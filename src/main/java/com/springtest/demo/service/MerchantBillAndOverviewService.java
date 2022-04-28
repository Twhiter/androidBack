package com.springtest.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.MerchantBillAndOverviewDao;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dto.BillRecord;
import com.springtest.demo.dto.Page;
import com.springtest.demo.entity.Merchant;
import com.springtest.demo.entity.MerchantBillAndOverview;
import com.springtest.demo.enums.BillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class MerchantBillAndOverviewService {

    @Autowired
    MerchantBillAndOverviewDao merchantBillAndOverviewDao;

    @Autowired
    MerchantDao merchantDao;


    public Page<MerchantBillAndOverview> getMerchantBillAndOverviewByPage(int merchantId, int pageSize, int pageNum,
                                                                          BigDecimal min, BigDecimal max, Date start,
                                                                          Date end,
                                                                          List<BillType> requestedBillTypes) {
        Page<MerchantBillAndOverview> page = new Page<>();
        page.pageSize = pageSize;
        page.currentPage = pageNum;


        var queryWrapper = new QueryWrapper<MerchantBillAndOverview>()
                .apply("abs(amount) between {0} and {1}", min, max)
                .eq("merchant_id", merchantId)
                .in("action", requestedBillTypes)
                .between("time", start, end)
                .orderByDesc("time");

        var count = merchantBillAndOverviewDao.selectCount(queryWrapper);

        var results = merchantBillAndOverviewDao.selectList(
                queryWrapper.last(String.format("limit %d,%d", (pageNum - 1) * pageSize, pageSize))
        );

        page.maxPage = (int) Math.ceil(1.0 * count / pageSize);
        page.data = results;

        return page;
    }


    public Page<BillRecord> getMerchantBillRecordByPage(int merchantId, int pageSize, int pageNum,
                                                        BigDecimal min, BigDecimal max, Date start,
                                                        Date end,
                                                        List<BillType> requestedBillTypes) {


        var merchantBillAndOverviewPage = this.getMerchantBillAndOverviewByPage
                (merchantId, pageSize, pageNum, min, max, start, end, requestedBillTypes);

        Page<BillRecord> page = new Page<>();

        page.data = merchantBillAndOverviewPage.data.stream().map(BillRecord::fromMerchantBillAndOverview).toList();
        page.maxPage = merchantBillAndOverviewPage.maxPage;
        page.pageSize = pageSize;
        page.currentPage = pageNum;

        return page;
    }


    public Page<BillRecord> getMerchantBillRecordByPageWithUserId(int userId, int pageSize, int pageNum,
                                                                  BigDecimal min, BigDecimal max, Date start,
                                                                  Date end,
                                                                  List<BillType> requestedBillTypes) {


        Merchant merchant = merchantDao.selectOne(new QueryWrapper<Merchant>().eq("merchant_user_id", userId));
        return this.getMerchantBillRecordByPage(merchant.merchantId, pageSize, pageNum, min, max, start, end, requestedBillTypes);
    }


}
