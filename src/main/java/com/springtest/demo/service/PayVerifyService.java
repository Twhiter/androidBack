package com.springtest.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dao.MerchantRsaPublicKeyDao;
import com.springtest.demo.dto.MerchantVerifyInfo;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.redisEntity.SessionPay;
import com.springtest.demo.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class PayVerifyService {

    @Autowired
    MerchantRsaPublicKeyDao merchantRsaPublicKeyDao;


    public MerchantVerifyInfo extractInfo(String RSAEncryptedBase64String) {

        try {
            byte[] RSAEncrypted = Base64.decodeBase64(RSAEncryptedBase64String);
            byte[] decrypted = RSAUtil.decrypt(RSAEncrypted, ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decrypted, MerchantVerifyInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Prompt verify(MerchantVerifyInfo merchantVerifyInfo, SessionPay sessionPay) {
        try {

            var data = merchantRsaPublicKeyDao.selectById(sessionPay.merchantId);
            if (data == null)
                return Prompt.pay_merchant_key_not_exist;

            String base64PublicKey = data.publicKey;
            byte[] signatureBytes = Base64.decodeBase64(merchantVerifyInfo.signature);
            byte[] supposedText = (merchantVerifyInfo.sessionId + "").getBytes(StandardCharsets.UTF_8);

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
