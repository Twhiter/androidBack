package com.springtest.demo.redisDao;

import com.springtest.demo.redisEntity.EmailVerifyCode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerifyCodeDao extends CrudRepository<EmailVerifyCode,String> {

}
