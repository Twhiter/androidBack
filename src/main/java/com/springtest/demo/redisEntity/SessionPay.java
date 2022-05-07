package com.springtest.demo.redisEntity;


import com.springtest.demo.enums.SessionPayState;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@Data
@RedisHash(timeToLive = 4 * 60)
public class SessionPay {

    @Id
    public Integer sessionId;

    public Integer merchantId;
    public Integer userId;
    public BigDecimal amount;
    public SessionPayState state = SessionPayState.un_paid;
    public String remarks;
    public String paymentPassword;
}
