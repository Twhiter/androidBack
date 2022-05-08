package com.springtest.demo;

import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.util.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.Semaphore;

public class NormalTest {


    @Test
    public void test4() throws IOException, InterruptedException {

        Semaphore semaphore = new Semaphore(1);


        var t1 = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        var t2 = new Thread(() -> {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();
            System.out.println("Done");

        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();


    }


    @Test
    public void Test5() {

        Date date = new Date();
        String format = "MMM dd yyyy";

        //Sun Apr 17 12:00:00 GMT+03:00 2022
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:");

        System.out.println(simpleDateFormat.format(date));


    }


    @Test
    public void Test6() throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, SignatureException, NoSuchAlgorithmException {


        String text = """
                {
                   "sessionId": -1173613415,
                   "signature": "hl1FyWg8dZkJKjM6H6dUEN4VOdTql5lhnc2iGCJjwcAlu9EAxTsVdMhBaixNSDeHY2JpUtin2NNMbH0gxhOrHzPYnuNk7+oH6n/a5XmCaOXB5o9R7yYdjiDGJY+dPxX/Bv74SGdog+BW/RlGsDQvnjFHF49bopZjNdZLbto+0oR5e4DleBHbIBsOSz2dI4WMGV3xVXjM2yDRiLH7B7HGtuStwD4Xz99SXFRAZIp7F0WXEBX9VbshQ3C9MvCe6uLbMlxQKUqZAAsa4mH6W0WsogFj/+pWX6UUu0C6oj6r8rNkrt9mXslJGogO+UVWrentA8MAcNtGu06r5Lw54DTq8g=="
                }
                                
                """;

        var signature = RSAUtil.sign(text.getBytes(StandardCharsets.UTF_8), ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);

        System.out.println(RSAUtil.verifySignature(signature, text.getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED));

        System.out.println(RSAUtil.verifySignature(signature, text.getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED));


        var encyrptedBytes = RSAUtil.encrypt(text.getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED);
        System.out.println(Arrays.toString(encyrptedBytes));

        var decyprtedBytes = RSAUtil.decrypt(encyrptedBytes, ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);
        System.out.println(Arrays.toString(decyprtedBytes));

        assert decyprtedBytes != null;
        System.out.println("equal original ? " + Arrays.equals(decyprtedBytes, text.getBytes(StandardCharsets.UTF_8)));


    }

    @Test
    public void Test7() throws InterruptedException {


        PaySemaphorePool pool = PaySemaphorePool.getInstance();

        pool.add(new PaySemaphore(123));

        var b = pool.get(123);

        b.sessionId = 3;

        System.out.println(pool.get(123).sessionId);
    }


    @Test
    public void Test8() {


        var pair = RSAUtil.generateRsaKey();
        assert pair != null;
        var privateKey = pair.getPrivate();
        var publicKey = pair.getPublic();


        var privateKeyBase64 = Base64.encodeBase64String(privateKey.getEncoded());
        var publicKeyBase64 = Base64.encodeBase64String(publicKey.getEncoded());


        System.out.println("publicKey:" + publicKeyBase64);
        System.out.println("privateKey:" + privateKeyBase64);

    }


    @Test
    public void Test9() {

        System.out.printf("%d,%s%n", 1, new BigDecimal("123.34"));
    }


}
