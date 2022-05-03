package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dao.UserDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.User;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.util.LambdaLogicChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    UserDao userDao;


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


}
