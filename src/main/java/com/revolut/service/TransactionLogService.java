package com.revolut.service;

import com.revolut.mapper.TransactionLogMapper;
import com.revolut.model.TransactionLog;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by zaskanov on 18.09.2017.
 */
public class TransactionLogService {

  @Inject
  private TransactionLogMapper transactionLogMapper;

  public boolean existsByExternalId(Long externalId) {
    Long count = transactionLogMapper.countByExternalId(externalId);
    return count != null && count > 0;
  }

  public List<TransactionLog> findByFromOrToId(Long id, Integer limit, Integer offset) {
    return transactionLogMapper.findByFromOrToId(id, limit, offset);
  }

  public void save(TransactionLog transaction) {
    transactionLogMapper.save(transaction);
  }
}
