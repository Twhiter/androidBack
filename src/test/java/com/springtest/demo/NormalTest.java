package com.springtest.demo;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Pipe;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

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




    }



}
