package com.springtest.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.dao.MerchantDao;
import com.springtest.demo.dao.MerchantRsaPublicKeyDao;
import com.springtest.demo.dto.RefundRequest;
import com.springtest.demo.entity.MerchantRsaPublicKey;
import com.springtest.demo.enums.Prompt;
import com.springtest.demo.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class RefundService {

    @Autowired
    MerchantDao merchantDao;

    @Autowired
    MerchantRsaPublicKeyDao merchantRsaPublicKeyDao;


    public RefundRequest extractInfo(String rsaBase64EncryptedString) {
        try {
            byte[] RSAEncrypted = Base64.decodeBase64(rsaBase64EncryptedString);
            byte[] decrypted = RSAUtil.decrypt(RSAEncrypted, ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(decrypted, RefundRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Prompt validateSignature(RefundRequest refundRequest) {

        try {
            MerchantRsaPublicKey merchantRsaKey = merchantRsaPublicKeyDao.selectById(refundRequest.merchantId);

            if (merchantRsaKey == null)
                return Prompt.refund_pay_merchant_not_exist;


            byte[] signatureBytes = Base64.decodeBase64(refundRequest.signature);


            byte[] supposedText = String.format("%d,%d", refundRequest.merchantId,
                    refundRequest.payId).getBytes(StandardCharsets.UTF_8);

            if (RSAUtil.verifySignature(signatureBytes, supposedText, merchantRsaKey.publicKey))
                return Prompt.success;
            else
                return Prompt.refund_wrong_rsa_keys;
        } catch (Exception e) {
            e.printStackTrace();
            return Prompt.unknownError;
        }
    }
}
