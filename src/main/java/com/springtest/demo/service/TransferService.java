package com.springtest.demo.service;

import com.springtest.demo.dao.TransferDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.entity.Transfer;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.State;
import com.springtest.demo.util.LambdaLogicChain;
import com.springtest.demo.util.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransferService {


    @Autowired
    TransferDao transferDao;

    @Autowired
    UserDao userDao;

    @Autowired
    TransactionHandler transactionHandler;

    public Object[] transfer(int sourceUserId, int targetUserId, BigDecimal amount,String paymentPassword,String remarks) {

        Prompt prompt = Prompt.transfer_error;
        Prompt[] returnedPrompt = new Prompt[]{prompt};
        Transfer transfer = null;


        try{
            transfer = transactionHandler.runInTransactionSerially(
                    () -> _transfer(sourceUserId,targetUserId,amount,paymentPassword,remarks,returnedPrompt));
        }catch (Exception e) {
            e.printStackTrace();
        }
        prompt = returnedPrompt[0];

        return new Object[]{prompt,transfer};
    }


    private Transfer _transfer(int sourceUserId,int targetUserId,BigDecimal amount,String paymentPassword,
                               String remarks,
                               Prompt[] returnedPrompt) {


        User sourceUser = userDao.selectById(sourceUserId);
        User targetUser = userDao.selectById(targetUserId);

        LambdaLogicChain<Prompt> logicChain = new LambdaLogicChain<>();

        returnedPrompt[0] = logicChain.process(
                () -> sourceUser == null? Prompt.transfer_source_not_exist:null,
                () -> targetUser == null? Prompt.transfer_target_not_exist:null,
                () -> !sourceUser.paymentPassword.equals(paymentPassword)?Prompt.payment_password_not_correct:null,
                () -> sourceUserId == targetUserId?Prompt.transfer_to_self_error:null,
                () -> amount.compareTo(BigDecimal.ZERO) <= 0?Prompt.transfer_amount_invalid_error:null,
                () -> sourceUser.state == State.frozen?Prompt.transfer_source_account_frozen:null,
                () -> sourceUser.state == State.unverified?Prompt.transfer_source_account_unverified:null,
                () -> targetUser.state == State.frozen?Prompt.transfer_target_account_frozen:null,
                () -> targetUser.state == State.unverified?Prompt.transfer_target_account_unverified:null,
                () -> sourceUser.moneyAmount.compareTo(amount) < 0? Prompt.transfer_not_enough_balance:null,
                () -> Prompt.transfer_error
        );

        if (returnedPrompt[0] != Prompt.transfer_error) {
            throw new RuntimeException(returnedPrompt[0].prompt);
        }

        //remove balance
        sourceUser.moneyAmount = sourceUser.moneyAmount.subtract(amount);
        userDao.updateById(sourceUser);

        //add balance
        targetUser.moneyAmount = targetUser.moneyAmount.add(amount);
        userDao.updateById(targetUser);

        //generate a new transfer
        Transfer transfer = new Transfer();
        transfer.amount = amount;
        transfer.remarks = remarks;
        transfer.sourceUserId = sourceUserId;
        transfer.targetUserId = targetUserId;

        transferDao.insert(transfer);

        returnedPrompt[0] = Prompt.success;

        return transferDao.selectById(transfer.transferId);
    }






}
