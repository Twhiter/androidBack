package com.springtest.demo.redisDao;

import com.springtest.demo.redisEntity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Mapper
public interface TokenDao extends CrudRepository<Token, String> {

    List<Token> findByToken(String token);
}
