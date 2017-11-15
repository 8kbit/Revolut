package com.revolut.mapper;

import com.revolut.model.Account;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * Created by zaskanov on 11.09.2017.
 */
public interface AccountMapper {

  Account findById(Long id);

  void save(Account account);

  int update(Account account);

  List<Account> list(@Param("limit") Integer limit, @Param("offset") Integer offset);
}
