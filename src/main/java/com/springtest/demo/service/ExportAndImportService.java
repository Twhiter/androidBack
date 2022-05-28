package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dao.MerchantExportUserDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dao.UserExportMerchantDao;
import com.springtest.demo.dto.ExportOrImport;
import com.springtest.demo.dto.Page;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.MerchantExportUser;
import com.springtest.demo.entity.User;
import com.springtest.demo.entity.UserExportMerchant;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.State;
import com.springtest.demo.enums.UserType;
import com.springtest.demo.util.LambdaLogicChain;
import com.springtest.demo.util.TransactionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ExportAndImportService {

    @Autowired
    private UserExportMerchantDao userExportMerchantDao;

    @Autowired
    private MerchantExportUserDao merchantExportUserDao;

    @Autowired
    TransactionHandler transactionHandler;
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private UserDao userDao;


    public Page<ExportOrImport> getAllExports(int pageSize, int pageNum) {

        Page<ExportOrImport> pageObj = new Page<>();
        pageObj.currentPage = pageNum;
        pageObj.pageSize = pageSize;


        pageObj.data = userExportMerchantDao.getAllExports(pageSize, pageNum);

        int count = Math.toIntExact(userExportMerchantDao.selectCount(null));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);

        return pageObj;
    }

    public Page<ExportOrImport> getAllImports(int pageSize, int pageNum) {

        Page<ExportOrImport> pageObj = new Page<>();
        pageObj.currentPage = pageNum;
        pageObj.pageSize = pageSize;


        pageObj.data = merchantExportUserDao.getAllImports(pageSize, pageNum);

        int count = Math.toIntExact(merchantExportUserDao.selectCount(null));

        pageObj.maxPage = (int) Math.ceil(1.0 * count / pageSize);

        return pageObj;
    }

    public Prompt importFromBank(UserType type, int id, BigDecimal amount) {

        return switch (type) {
            case user -> userImportFromBank(id, amount);
            case merchant -> merchantImportFromBank(id, amount);
            default -> Prompt.unknownError;
        };
    }


    public Prompt exportToBank(UserType type, int id, BigDecimal amount, String paymentPassword) {

        return switch (type) {
            case user -> userExportToBank(id, amount, paymentPassword);
            case merchant -> merchantExportToBank(id, amount, paymentPassword);
            default -> Prompt.unknownError;
        };
    }


    private Prompt merchantImportFromBank(int id, BigDecimal amount) {

        LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();
        User user = userDao.selectById(id);
        Merchant merchant = merchantDao.selectOne(
                new QueryWrapper<Merchant>().eq("merchant_user_id", user.userId));

        Prompt prompt = chain.process(
                () -> merchant == null ? Prompt.import_target_not_exist : null,
                () -> merchant.state == State.frozen ? Prompt.import_target_frozen : null,
                () -> merchant.state == State.unverified ? Prompt.import_target_unverified : null);

        if (prompt != null)
            return prompt;

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);
        merchantDao.update(null, new UpdateWrapper<Merchant>()
                .setSql("money_amount=money_amount + " + amount.toString())
                .eq("merchant_id", merchant.merchantId)
        );

        return Prompt.success;
    }

    private Prompt userImportFromBank(int id, BigDecimal amount) {

        LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();
        User user = userDao.selectById(id);

        Prompt prompt = chain.process(
                () -> user == null ? Prompt.import_target_not_exist : null,
                () -> user.state == State.frozen ? Prompt.import_target_frozen : null,
                () -> user.state == State.unverified ? Prompt.import_target_unverified : null);

        if (prompt != null)
            return prompt;

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);
        userDao.update(null, new UpdateWrapper<User>()
                .setSql("money_amount=money_amount + " + amount.toString())
                .eq("user_id", id)
        );
        return Prompt.success;
    }

    private Prompt merchantExportToBank(int id, BigDecimal amount, String paymentPassword) {

        User user = userDao.selectById(id);
        Merchant merchant = merchantDao.selectOne(
                new QueryWrapper<Merchant>().eq("merchant_user_id", user.userId));

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);


        LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();
        Prompt prompt = chain.process(
                () -> merchant.state == State.frozen ? Prompt.export_target_frozen : null,
                () -> merchant.state == State.unverified ? Prompt.export_target_unverified : null,
                () -> !paymentPassword.equals(user.paymentPassword) ? Prompt.export_wrong_password : null
        );

        if (prompt != null)
            return prompt;


        merchantDao.update(null, new UpdateWrapper<Merchant>()
                .setSql("money_amount=money_amount - " + amount.toString())
                .eq("merchant_id", merchant.merchantId)
        );

        return Prompt.success;
    }


    private Prompt userExportToBank(int id, BigDecimal amount, String paymentPassword) {

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);
        User user = userDao.selectById(id);

        LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();
        Prompt prompt = chain.process(
                () -> user.state == State.frozen ? Prompt.export_target_frozen : null,
                () -> user.state == State.unverified ? Prompt.export_target_unverified : null,
                () -> !paymentPassword.equals(user.paymentPassword) ? Prompt.export_wrong_password : null
        );

        if (prompt != null)
            return prompt;

        userDao.update(null, new UpdateWrapper<User>()
                .setSql("money_amount=money_amount - " + amount.toString())
                .eq("user_id", id)
        );

        return Prompt.success;

    }


    public Prompt exportToMerchant(int userId, BigDecimal amount) {

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);


        BigDecimal finalAmount = amount;
        return transactionHandler.runInNewTransaction(() -> {

            User user = userDao.selectById(userId);
            if (user == null)
                return Prompt.ei_user_not_exist;

            Merchant merchant = merchantDao.selectOne(new QueryWrapper<Merchant>()
                    .eq("merchant_user_id", userId));

            LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();

            Prompt prompt = chain.process(
                    () -> user.state == State.unverified ? Prompt.ei_user_unverified : null,
                    () -> user.state == State.frozen ? Prompt.ei_user_frozen_state : null,
                    () -> merchant == null ? Prompt.ei_merchant_not_exist : null,
                    () -> merchant.state == State.unverified ? Prompt.ei_merchant_unverified : null,
                    () -> merchant.state == State.frozen ? Prompt.ei_merchant_frozen_state : null,
                    () -> user.moneyAmount.compareTo(finalAmount) < 0 ? Prompt.ei_not_enough_balance : null
            );

            if (prompt != null)
                return prompt;

            //remove user's balance
            user.moneyAmount = user.moneyAmount.subtract(finalAmount);
            userDao.updateById(user);

            //add merchant balance
            merchant.moneyAmount = merchant.moneyAmount.add(finalAmount);
            merchantDao.updateById(merchant);

            //generate a record
            UserExportMerchant export = new UserExportMerchant();
            export.amount = finalAmount;
            export.userId = userId;
            userExportMerchantDao.insert(export);

            return Prompt.success;
        });
    }


    public Prompt exportToUser(int userId, BigDecimal amount) {


        amount = amount.setScale(2, RoundingMode.UNNECESSARY);


        BigDecimal finalAmount = amount;
        return transactionHandler.runInNewTransaction(() -> {

            User user = userDao.selectById(userId);
            if (user == null)
                return Prompt.ei_user_not_exist;

            Merchant merchant = merchantDao.selectOne(new QueryWrapper<Merchant>()
                    .eq("merchant_user_id", userId));

            LambdaLogicChain<Prompt> chain = new LambdaLogicChain<>();

            Prompt prompt = chain.process(
                    () -> user.state == State.unverified ? Prompt.ei_user_unverified : null,
                    () -> user.state == State.frozen ? Prompt.ei_user_frozen_state : null,
                    () -> merchant == null ? Prompt.ei_merchant_not_exist : null,
                    () -> merchant.state == State.unverified ? Prompt.ei_merchant_unverified : null,
                    () -> merchant.state == State.frozen ? Prompt.ei_merchant_frozen_state : null,
                    () -> merchant.moneyAmount.compareTo(finalAmount) < 0 ? Prompt.ei_not_enough_balance : null
            );

            if (prompt != null)
                return prompt;

            //remove merchant balance
            merchant.moneyAmount = merchant.moneyAmount.subtract(finalAmount);
            merchantDao.updateById(merchant);

            //add user's balance
            user.moneyAmount = user.moneyAmount.add(finalAmount);
            userDao.updateById(user);

            //generate a record
            MerchantExportUser export = new MerchantExportUser();
            export.merchantId = merchant.merchantId;
            export.amount = finalAmount;
            merchantExportUserDao.insert(export);

            return Prompt.success;
        });

    }
}
