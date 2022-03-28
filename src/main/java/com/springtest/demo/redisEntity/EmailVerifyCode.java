package com.springtest.demo.redisEntity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Data
@RedisHash(timeToLive = 60 * 5)
public class EmailVerifyCode {

    @Id
    private String email;

    private String code;
    @TimeToLive
    private Integer timeout;
}
