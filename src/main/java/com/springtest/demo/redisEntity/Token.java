package com.springtest.demo.redisEntity;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.util.concurrent.TimeUnit;

@Data
@RedisHash
public class Token {

    @Id
    public String id;

    @Indexed
    public String token;

    @TimeToLive(unit = TimeUnit.DAYS)
    public Long timeout = 5L;
}
