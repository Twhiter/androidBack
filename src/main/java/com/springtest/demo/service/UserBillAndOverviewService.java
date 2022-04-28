package com.springtest.demo.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.springtest.demo.dao.UserBillAndOverviewDao;
import com.springtest.demo.dto.BillRecord;
import com.springtest.demo.dto.Page;
import com.springtest.demo.entity.UserBillAndOverview;
import com.springtest.demo.enums.BillType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserBillAndOverviewService {

    @Autowired
    UserBillAndOverviewDao userBillAndOverviewDao;


    public Page<UserBillAndOverview> getUserBillAndOverviewByPage(int userId, int pageSize, int pageNum,
                                                                  BigDecimal min, BigDecimal max, Date start,
                                                                  Date end,
                                                                  List<BillType> requestedBillTypes) {
        Page<UserBillAndOverview> page = new Page<>();
        page.pageSize = pageSize;
        page.currentPage = pageNum;


        var queryWrapper = new QueryWrapper<UserBillAndOverview>()
                .apply("abs(amount) between {0} and {1}", min, max)
                .eq("user_id", userId)
                .in("action", requestedBillTypes)
                .between("time", start, end)
                .orderByDesc("time");

        var count = userBillAndOverviewDao.selectCount(queryWrapper);

        var results = userBillAndOverviewDao.selectList(
                queryWrapper.last(String.format("limit %d,%d", (pageNum - 1) * pageSize, pageSize))
        );

        page.maxPage = (int) Math.ceil(1.0 * count / pageSize);
        page.data = results;

        return page;
    }

    public Page<BillRecord> getUserBillRecordByPage(int userId, int pageSize, int pageNum,
                                                    BigDecimal min, BigDecimal max, Date start, Date end,
                                                    List<BillType> requestedBillTypes) {


        var userBillAndOverviewPage = this.getUserBillAndOverviewByPage
                (userId, pageSize, pageNum, min, max, start, end, requestedBillTypes);

        Page<BillRecord> page = new Page<>();

        page.data = userBillAndOverviewPage.data.stream().map(BillRecord::fromUserBillAndOverview).toList();
        page.maxPage = userBillAndOverviewPage.maxPage;
        page.pageSize = pageSize;
        page.currentPage = pageNum;

        return page;
    }


}
