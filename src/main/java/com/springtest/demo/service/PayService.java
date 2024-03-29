package com.springtest.demo.service;


import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dao.PayDao;
import com.springtest.demo.dao.RefundDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.PaymentWithRefund;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.Pay;
import com.springtest.demo.entity.Refund;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.PayState;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.State;
import com.springtest.demo.redisEntity.SessionPay;
import com.springtest.demo.util.LambdaLogicChain;
import com.springtest.demo.util.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PayService {

    @Autowired
    PayDao payDao;

    @Autowired
    UserDao userDao;

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    TransactionHandler transactionHandler;

    @Autowired
    RefundDao refundDao;

    public Object[] pay(int userId, int merchantId, BigDecimal amount, String paymentPassword, String remarks) {

        Prompt prompt = Prompt.pay_error;
        Prompt[] returnedPrompt = new Prompt[]{prompt};
        Pay pay = null;

        try {
            pay = transactionHandler.runInTransactionSerially(() -> _pay(userId, merchantId, amount, paymentPassword, remarks,
                    returnedPrompt));
        } catch (Exception e) {
            e.printStackTrace();
        }

        prompt = returnedPrompt[0];

        return new Object[]{prompt, pay};
    }

    private Pay _pay(int userId, int merchantId, BigDecimal amount, String paymentPassword, String remarks,
                     Prompt[] returnedPrompt) {
        User user = userDao.selectById(userId);
        Merchant merchant = merchantDao.selectById(merchantId);


        LambdaLogicChain<Prompt> logicChain = new LambdaLogicChain<>();

        returnedPrompt[0] = logicChain.process(
                () -> user == null ? Prompt.pay_user_not_found_error : null,
                () -> merchant == null ? Prompt.pay_merchant_not_found_error : null,
                () -> !user.paymentPassword.equals(paymentPassword) ? Prompt.payment_password_not_correct : null,
                () -> amount.compareTo(BigDecimal.ZERO) <= 0 ? Prompt.pay_amount_invalid_error : null,
                () -> user.state == State.frozen ? Prompt.pay_user_account_frozen : null,
                () -> user.state == State.unverified ? Prompt.pay_user_account_unverified : null,
                () -> user.userId.equals(merchant.merchantUserId) ? Prompt.pay_user_to_self_error : null,
                () -> merchant.state == State.unverified ? Prompt.pay_merchant_account_unverified : null,
                () -> merchant.state == State.frozen ? Prompt.pay_merchant_account_frozen : null,
                () -> user.moneyAmount.compareTo(amount) < 0 ? Prompt.pay_user_not_enough_balance : null,
                () -> Prompt.pay_error
        );

        if (returnedPrompt[0] != Prompt.pay_error) {
            throw new RuntimeException();
        }

        //remove balance from user
        user.moneyAmount = user.moneyAmount.subtract(amount).setScale(4, RoundingMode.HALF_UP);

        userDao.updateById(user);

        //insert a pay
        Pay pay = new Pay();
        pay.amount = amount;
        pay.fee = amount.multiply(ConfigUtil.FEE_RATE).setScale(4, RoundingMode.HALF_UP);
        pay.sourceUserId = userId;
        pay.targetMerchantId = merchantId;
        pay.remarks = remarks;

        payDao.insert(pay);

        //add balance to merchant
        merchant.moneyAmount = merchant.moneyAmount.add(amount.subtract(pay.fee));
        merchantDao.updateById(merchant);

        returnedPrompt[0] = Prompt.success;

        return payDao.selectById(pay.payId);
    }


    public Object[] payWithConfirm(SessionPay sessionPay) {

        Prompt prompt = Prompt.pay_error;
        Prompt[] returnedPrompt = new Prompt[]{prompt};
        AtomicReference<Pay> pay = new AtomicReference<>();

        try {
            transactionHandler.runInTransactionSerially(() -> {
                pay.set(_pay(sessionPay.userId, sessionPay.merchantId, sessionPay.amount, sessionPay.paymentPassword,
                        sessionPay.remarks, returnedPrompt));
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        prompt = returnedPrompt[0];

        return new Object[]{prompt, pay.get()};
    }


    public Page<PaymentWithRefund> getAllPays(int pageSize, int pageNum) {
        Page<PaymentWithRefund> pageObj = new Page<>();

        var data = payDao.getAllPays(pageNum, pageSize);

        pageObj.currentPage = pageNum;
        pageObj.pageSize = pageSize;
        pageObj.data = data;

        int count = Math.toIntExact(payDao.selectCount(null));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);


        return pageObj;
    }


    public Prompt refundPayWithMerchantId(int merchantId, int payId) {

        LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();

        Merchant m = merchantDao.selectById(merchantId);
        Pay p = payDao.selectById(payId);


        Prompt prompt = chain.process(
                () -> m == null ? Prompt.refund_pay_merchant_not_exist : null,
                () -> p == null ? Prompt.refund_pay_id_not_exist : null,
                () -> p.targetMerchantId != merchantId ? Prompt.refuned_pay_merchant_not_enough_right_refund : null);
        if (prompt != null)
            return prompt;

        prompt = refundPay(payId);
        return prompt;
    }


    public Prompt refundPay(int payId) {

        try {
            return transactionHandler.runInNewTransactionSerially(() -> {
                Pay pay = payDao.selectById(payId);
                if (pay == null)
                    return Prompt.refund_pay_id_not_exist;
                else if (pay.state == PayState.refunded)
                    return Prompt.refund_pay_id_already_refunded;

                User user = userDao.selectById(pay.sourceUserId);
                Merchant merchant = merchantDao.selectById(pay.targetMerchantId);

                // compute the money to refund from merchant account
                BigDecimal amount = pay.amount.subtract(pay.fee);
                merchant.moneyAmount = merchant.moneyAmount.subtract(amount);

                merchantDao.updateById(merchant);

                //refund to the user account
                user.moneyAmount = user.moneyAmount.add(pay.amount);
                userDao.updateById(user);

                //insert a refund record
                Refund refund = new Refund();
                refund.payId = payId;

                refundDao.insert(refund);

                //change the state of the pay
                pay.state = PayState.refunded;

                payDao.updateById(pay);

                return Prompt.success;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }
}
