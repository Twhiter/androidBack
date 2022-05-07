package com.springtest.demo.businessEntity;

import java.util.concurrent.Semaphore;

public class PaySemaphore {

    public int sessionId;
    public Semaphore notScanned = new Semaphore(1);
    public Semaphore isPaid = new Semaphore(1);
    public Semaphore isFinished = new Semaphore(1);

    public PaySynData paySynData = new PaySynData();

    public PaySemaphore(int sessionId) throws InterruptedException {
        this.sessionId = sessionId;

        isPaid.acquire();
        isFinished.acquire();
    }
}
