package com.revolut.service;

import com.revolut.mapper.TransactionLogMapper;
import com.revolut.model.TransactionLog;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by zaskanov on 18.09.2017.
 */
public class TransactionLogService {
    @Inject
    private TransactionLogMapper transactionLogMapper;

    public List<TransactionLog> findByFromOrToId(Long id, Integer limit, Integer offset) {
        return transactionLogMapper.findByFromOrToId(id, limit, offset);
    }

    public void save(TransactionLog transaction) {
        transactionLogMapper.save(transaction);
    }
}
