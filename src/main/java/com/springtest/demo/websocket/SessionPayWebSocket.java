package com.springtest.demo.websocket;

import com.springtest.demo.businessEntity.PaySemaphore;
import com.springtest.demo.businessEntity.PaySemaphorePool;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


//todo need a test
public class SessionPayWebSocket extends TextWebSocketHandler {

    Thread phonePaidListener = null;
    Thread successListener = null;
    Thread timer = null;
    private int sessionPayId = 0;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        session.setTextMessageSizeLimit(200000);


        try {
            sessionPayId = extractSessionPayId(session);

            PaySemaphore paySemaphore = PaySemaphorePool.getInstance().get(sessionPayId);
            if (paySemaphore.isWebSocketConnected) {
                closeSession(session);
                return;
            } else paySemaphore.isWebSocketConnected = true;


            phonePaidListener = new Thread(() -> sendPhonePaidMessage(session));
            successListener = new Thread(() -> sendSuccessMessage(session));
            timer = new Thread(() -> {

                try {
                    Thread.sleep(150 * 60);
                    closeSession(session);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });

            phonePaidListener.start();
            successListener.start();
            timer.start();
        } catch (Exception e) {
            e.printStackTrace();
            closeSession(session);
        }


    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {

        PaySemaphore paySemaphore = PaySemaphorePool.getInstance().get(sessionPayId);
        paySemaphore.isWebSocketConnected = false;

        if (phonePaidListener != null && phonePaidListener.isAlive())
            phonePaidListener.stop();

        if (successListener != null && successListener.isAlive())
            successListener.stop();

        if (timer != null && timer.isAlive())
            timer.stop();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        super.handleTransportError(session, exception);
        exception.printStackTrace();
        closeSession(session);
    }


    private void closeSession(WebSocketSession session) {
        if (session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //from websocket url extracting sessionId in the * part
    private int extractSessionPayId(WebSocketSession socketSession) {

        String sessionUri = Objects.requireNonNull(socketSession.getUri()).toString();

        String sessionPayIdStr = sessionUri.substring(sessionUri.lastIndexOf("/") + 1);
        return Integer.parseInt(sessionPayIdStr);
    }


    private void sendPhonePaidMessage(WebSocketSession socketSession) {

        try {
            PaySemaphorePool pool = PaySemaphorePool.getInstance();

            PaySemaphore paySemaphore = pool.get(sessionPayId);

            if (paySemaphore == null) {
                closeSession(socketSession);
                return;
            }

            var isOkay = paySemaphore.isPaid.tryAcquire(2, TimeUnit.MINUTES);
            if (!isOkay) {
                closeSession(socketSession);
                return;
            }

            paySemaphore.isPaid.release();

            socketSession.sendMessage(new TextMessage(sessionPayId + ""));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendSuccessMessage(WebSocketSession socketSession) {

        try {
            PaySemaphorePool pool = PaySemaphorePool.getInstance();

            PaySemaphore paySemaphore = pool.get(sessionPayId);

            if (paySemaphore == null) {
                closeSession(socketSession);
                return;
            }

            var isOkay = paySemaphore.isFinished.tryAcquire(2, TimeUnit.MINUTES);
            if (!isOkay) {
                closeSession(socketSession);
                return;
            }

            paySemaphore.isFinished.release();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
