package com.springtest.demo.service;

import com.springtest.demo.Util;
import com.springtest.demo.redisDao.TokenDao;
import com.springtest.demo.redisEntity.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {


   @Autowired
   TokenDao tokenDao;


   public void saveToken(Token token) {
       tokenDao.save(token);
   }



   public String verify(String token) {

       var result = tokenDao.findByToken(token);

       if (result.size() == 1)
           return result.get(0).id;
       else
           return null;
   }

}
