package com.springtest.demo;

import com.springtest.demo.config.ConfigUtil;
import com.springtest.demo.util.RSAUtil;
import org.junit.jupiter.api.Test;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
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


        String text = "Hello World";

        var signature = RSAUtil.sign(text.getBytes(StandardCharsets.UTF_8), ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);

        System.out.println(RSAUtil.verifySignature(signature, text.getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED));

        System.out.println(RSAUtil.verifySignature(signature, "Hello world".getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED));


        var encyrptedBytes = RSAUtil.encrypt(text.getBytes(StandardCharsets.UTF_8),
                ConfigUtil.PUBLIC_KEY_BASE64_ENCODED);
        System.out.println(Arrays.toString(encyrptedBytes));

        var decyprtedBytes = RSAUtil.decrypt(encyrptedBytes, ConfigUtil.PRIVATE_KEY_BASE64_ENCODED);
        System.out.println(Arrays.toString(decyprtedBytes));

        assert decyprtedBytes != null;
        System.out.println("equal original ? " + Arrays.equals(decyprtedBytes, text.getBytes(StandardCharsets.UTF_8)));


    }


}
