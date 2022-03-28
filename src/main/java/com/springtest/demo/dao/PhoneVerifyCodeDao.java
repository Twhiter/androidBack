package com.springtest.demo.dao;


import com.springtest.demo.redisEntity.PhoneVerifyCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneVerifyCodeDao extends CrudRepository<PhoneVerifyCode,String> {
}
