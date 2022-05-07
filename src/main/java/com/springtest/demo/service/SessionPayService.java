package com.springtest.demo.service;

import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.SessionPayState;
import com.springtest.demo.redisDao.SessionPayDao;
import com.springtest.demo.redisEntity.SessionPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionPayService {


    @Autowired
    SessionPayDao sessionPayDao;


    public Prompt check(int sessionId) {

        if (sessionPayDao.existsById(sessionId))
            return Prompt.pay_session_id_error;
        else
            return null;
    }


    public Prompt phoneScan(int sessionId, int userId, String remarks, String paymentPassword) {

        SessionPay sessionPay;
        var opt = sessionPayDao.findById(sessionId);

        if (opt.isPresent())
            sessionPay = opt.get();
        else
            return Prompt.pay_session_id_error;


        sessionPay.userId = userId;
        sessionPay.remarks = remarks;
        sessionPay.state = SessionPayState.paid_waiting_for_verified;
        sessionPay.paymentPassword = paymentPassword;

        sessionPayDao.save(sessionPay);
        return Prompt.success;
    }


    public SessionPay getById(int sessionId) {
        return sessionPayDao.findById(sessionId).orElse(null);
    }


}
