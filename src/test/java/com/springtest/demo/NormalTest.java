package com.springtest.demo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
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






}
