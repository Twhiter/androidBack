package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.dto.Page;
import com.springtest.demo.dto.UserAndMerchant;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.State;
import com.springtest.demo.util.LambdaLogicChain;
import com.springtest.demo.util.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class MerchantService {

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    UserDao userDao;

    @Autowired
    TransactionHandler transactionHandler;


    public Merchant getMerchantByUserId(int userId) {

        return merchantDao.selectOne(new QueryWrapper<Merchant>()
                .eq("merchant_user_id", userId));
    }

    public Merchant getMerchantById(int id) {
        return merchantDao.selectById(id);
    }


    public OverviewInfo getMerchantOverview(int id) {

        Merchant merchant = getMerchantById(id);
        return OverviewInfo.fromMerchant(merchant);
    }

    public Prompt registerMerchant(Merchant merchant) {

        try {
            User user = null;

            //first check whether the merchant bind phone,email to its user account
            if (merchant.merchantPhoneNumber.equals("")) {
                user = userDao.selectById(merchant.merchantUserId);
                merchant.merchantPhoneNumber = user.phoneNumber;
            }

            if (merchant.merchantEmail.equals("")) {
                if (user == null)
                    user = userDao.selectById(merchant.merchantUserId);
                merchant.merchantEmail = user.email;
            }


            LambdaLogicChain<Prompt> logicChain = new LambdaLogicChain<>();

            Prompt p = logicChain.process(
                    () -> {

                        if (merchantDao.exists(new QueryWrapper<Merchant>().eq("merchant_user_id"
                                , merchant.merchantUserId)))
                            return Prompt.duplicate_register;
                        else if (merchantDao.exists(new QueryWrapper<Merchant>().eq("merchant_phone_number"
                                , merchant.merchantPhoneNumber)))
                            return Prompt.merchant_phoneNumber_already_exist;
                        else if (merchantDao.exists(new QueryWrapper<Merchant>().eq("merchant_license"
                                , merchant.merchantLicense)))
                            return Prompt.merchant_license_number_already_exist;
                        else
                            return null;
                    }
            );

            if (p != null)
                return p;

            merchantDao.insert(merchant);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }


    public Page<UserAndMerchant> getMerchants(int pageNumber, int pageSize) {


        Page<UserAndMerchant> pageObj = new Page<>();

        var data = merchantDao.getMerchantAndUser(pageSize, pageNumber);

        pageObj.currentPage = pageNumber;
        pageObj.pageSize = pageSize;
        pageObj.data = data.stream().map(UserAndMerchant::fromUserJoinMerchant).toList();

        int count = Math.toIntExact(merchantDao.selectCount(new QueryWrapper<Merchant>().ne("state", State.unverified)));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);


        return pageObj;
    }


    public Prompt freezeMerchant(int merchantId) {

        try {
            Merchant merchant = merchantDao.selectById(merchantId);

            LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();


            var prompt = chain.process(() -> merchant == null ? Prompt.freeze_merchant_not_exist : null,
                    () -> merchant.state == State.unverified ? Prompt.freeze_merchant_account_not_verified : null);

            if (prompt != null)
                return prompt;

            merchantDao.update(null,
                    new UpdateWrapper<Merchant>().set("state", State.frozen).eq("merchant_id", merchantId));
            return Prompt.success;
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }

    public Prompt unfreezeMerchant(int merchantId) {

        try {
            Merchant merchant = merchantDao.selectById(merchantId);

            LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();


            var prompt = chain.process(() -> merchant == null ? Prompt.unfreeze_merchant_not_exist_error : null,
                    () -> merchant.state == State.unverified ? Prompt.unfreeze_merchant_account_not_verified : null
            );

            if (prompt != null)
                return prompt;

            merchantDao.update(null,
                    new UpdateWrapper<Merchant>().set("state", State.normal).eq("merchant_id", merchantId));
            return Prompt.success;
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }

    }


    public Prompt freezeMerchantBalance(int merchantId, BigDecimal amount) {

        try {

            BigDecimal formattedAmount = amount.setScale(2, RoundingMode.UNNECESSARY);
            return transactionHandler.runInNewTransaction(() -> {


                Merchant merchant = merchantDao.selectById(merchantId);

                LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();

                Prompt result = chain.process(
                        () -> merchant == null ? Prompt.freeze_merchant_balance_merchant_not_exist : null,
                        () -> merchant.state == State.unverified ? Prompt.freeze_merchant_balance_merchant_account_not_verified : null,
                        () -> merchant.moneyAmount.compareTo(formattedAmount) < 0 ? Prompt.freeze_merchant_balance_merchant_not_enough_balance : null,
                        () -> formattedAmount.compareTo(BigDecimal.ZERO) == 0 ? Prompt.invalid_amount : null
                );

                if (result != null)
                    return result;


                merchant.moneyAmount = merchant.moneyAmount.subtract(formattedAmount);
                merchant.frozenMoney = merchant.frozenMoney.add(formattedAmount);

                merchantDao.updateById(merchant);
                return Prompt.success;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }


    public Prompt unfreezeMerchantBalance(int merchantId, BigDecimal amount) {

        try {

            BigDecimal formattedAmount = amount.setScale(2, RoundingMode.UNNECESSARY);
            return transactionHandler.runInNewTransaction(() -> {


                Merchant merchant = merchantDao.selectById(merchantId);

                LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();

                Prompt result = chain.process(
                        () -> merchant == null ? Prompt.freeze_merchant_balance_merchant_not_exist : null,
                        () -> merchant.state == State.unverified ? Prompt.freeze_merchant_balance_merchant_account_not_verified : null,
                        () -> merchant.frozenMoney.compareTo(formattedAmount) < 0 ? Prompt.unfreeze_merchant_balance_frozen_amount_not_enough : null,
                        () -> formattedAmount.compareTo(BigDecimal.ZERO) == 0 ? Prompt.invalid_amount : null
                );

                if (result != null)
                    return result;


                merchant.moneyAmount = merchant.moneyAmount.add(formattedAmount);
                merchant.frozenMoney = merchant.frozenMoney.subtract(formattedAmount);

                merchantDao.updateById(merchant);
                return Prompt.success;
            });
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }


}
