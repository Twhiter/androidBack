package com.springtest.demo.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class TransactionHandler {

    @Transactional(propagation = Propagation.REQUIRED,rollbackFor = Exception.class)
    public <T> T runInTransaction(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public <T> T runInNewTransaction(Supplier<T> supplier) {
        return supplier.get();
    }


    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public <T> T runInTransactionSerially(Supplier<T> supplier) {
        return supplier.get();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,isolation = Isolation.SERIALIZABLE,rollbackFor = Exception.class)
    public <T> T runInNewTransactionSerially(Supplier<T> supplier) {
        return supplier.get();
    }
}
