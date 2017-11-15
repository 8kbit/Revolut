package com.revolut.service.test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.config.MyBatisModule;
import com.revolut.config.ServletConfig;
import com.revolut.config.TransferModule;
import com.revolut.model.Account;
import com.revolut.service.AccountService;
import java.io.IOException;
import java.math.BigDecimal;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Created by zaskanov on 01.04.2017.
 */
@Test
public class AccountServiceTest {

  Injector injector;

  @BeforeClass
  public void before() {
    ServletConfig config = new ServletConfig() {
      @Override
      public Injector getInjector() {
        Injector injector = Guice.createInjector(new TransferModule(), new MyBatisModule());

        migrateDB(injector);

        return injector;
      }
    };
    injector = config.getInjector();
  }

  @Test(expectedExceptions = RuntimeException.class)
  public void accountConcurrentModificationControlTest() throws IOException {
    AccountService accountService = injector.getInstance(AccountService.class);

    Account account = new Account("boris", BigDecimal.valueOf(100));
    accountService.save(account);

    Account copy = accountService.findById(account.getId());
    copy.setBalance(BigDecimal.valueOf(50));
    copy.setBalance(BigDecimal.valueOf(150));

    accountService.update(account);
    accountService.update(copy);
  }
}
