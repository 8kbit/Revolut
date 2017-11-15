package com.revolut.service;

import com.revolut.mapper.AccountMapper;
import com.revolut.model.Account;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by zaskanov on 18.09.2017.
 */
public class AccountService {

  @Inject
  private AccountMapper accountMapper;

  public Account findById(Long id) {
    return accountMapper.findById(id);
  }

  public List<Account> list(Integer limit, Integer offset) {
    return accountMapper.list(limit, offset);
  }

  public void save(Account account) {
    accountMapper.save(account);
    account.setVersion(account.getVersion() + 1);
  }

  public void update(Account account) {
    int res = accountMapper.update(account);
    if (res == 0) {
      throw new RuntimeException("trying to update database with obsolete Entity");
    }
    account.setVersion(account.getVersion() + 1);
  }
}
