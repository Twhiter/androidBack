package com.springtest.demo.redisEntity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@RedisHash(timeToLive = 5 * 60)

public class PhoneVerifyCode {
    @Id
    private String phone;
    private String code;

    @TimeToLive
    private Long Timeout;

}
