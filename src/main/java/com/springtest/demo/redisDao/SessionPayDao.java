package com.springtest.demo.redisDao;

import com.springtest.demo.redisEntity.SessionPay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionPayDao extends CrudRepository<SessionPay, Integer> {
}
