package com.springtest.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dao.MerchantRsaPublicKeyDao;
import com.springtest.demo.dto.SessionPayRequest;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.enums.SessionPayState;
import com.springtest.demo.redisDao.SessionPayDao;
import com.springtest.demo.redisEntity.SessionPay;
import com.springtest.demo.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Service
public class SessionPayService {


    @Autowired
    SessionPayDao sessionPayDao;

    @Autowired
    MerchantRsaPublicKeyDao merchantRsaPublicKeyDao;


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

    public SessionPay initialize(int merchantId, BigDecimal amount) {
        SessionPay sessionPay = new SessionPay();
        sessionPay.amount = amount;
        sessionPay.merchantId = merchantId;
        sessionPay.state = SessionPayState.un_paid;

        return sessionPayDao.save(sessionPay);
    }


    public SessionPayRequest extractInfo(String RSAEncryptedBase64String) {
        try {
            byte[] RSAEncrypted = Base64.decodeBase64(RSAEncryptedBase64String);
            byte[] decrypted = RSAUtil.decrypt(RSAEncrypted, ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decrypted, SessionPayRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Prompt verifySessionRequest(SessionPayRequest sessionPayRequest) {

        try {

            var data = merchantRsaPublicKeyDao.selectById(sessionPayRequest.merchantId);
            if (data == null)
                return Prompt.pay_merchant_key_not_exist;

            String base64PublicKey = data.publicKey;
            byte[] signatureBytes = Base64.decodeBase64(sessionPayRequest.signature);


            byte[] supposedText = String.format("%d,%s", sessionPayRequest.merchantId, sessionPayRequest.amount)
                    .getBytes(StandardCharsets.UTF_8);

            if (RSAUtil.verifySignature(signatureBytes, supposedText, base64PublicKey))
                return Prompt.success;
            else
                return Prompt.pay_verify_wrong_signature;
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }


    }


}
