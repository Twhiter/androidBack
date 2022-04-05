package com.springtest.demo.redisDao;

import com.springtest.demo.redisEntity.Token;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenDao extends CrudRepository<Token,Integer> {

    List<Token> findByToken(String token);
}
