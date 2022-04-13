package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dto.OverviewInfo;
import com.springtest.demo.entity.Merchant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MerchantService {

    @Autowired
    MerchantDao merchantDao;


    public Merchant getMerchantByUserId(int userId) {

        return merchantDao.selectOne(new QueryWrapper<Merchant>()
                .eq("merchant_user_id",userId));
    }

    public Merchant getMerchantById(int id) {
        return merchantDao.selectById(id);
    }


    public OverviewInfo getMerchantOverview(int id) {

        Merchant merchant = getMerchantById(id);
        return new OverviewInfo(merchant.merchantLogo,merchant.merchantName,merchant.merchantPhoneNumber);
    }


}
