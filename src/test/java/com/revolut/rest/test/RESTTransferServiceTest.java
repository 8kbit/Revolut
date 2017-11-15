package com.revolut.rest.test;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.config.MyBatisModule;
import com.revolut.config.ServletConfig;
import com.revolut.config.TransferModule;
import com.revolut.model.Account;
import com.revolut.model.TransactionLog;
import com.revolut.rest.RESTTransferService;
import com.revolut.service.AccountService;
import com.revolut.service.TransactionLogService;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.ws.rs.core.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by zaskanov on 18.09.2017.
 */
public class RESTTransferServiceTest {

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

  @Test
  public void transferTest() throws IOException {
    AccountService accountService = injector.getInstance(AccountService.class);

    Account mike = new Account("Mike", BigDecimal.valueOf(100));
    Account boris = new Account("Boris", BigDecimal.valueOf(100));

    accountService.save(mike);
    accountService.save(boris);

    RESTTransferService restTransferService = injector.getInstance(RESTTransferService.class);
    Response response;

    //test from id is null or not exists
    response = restTransferService.transfer(null, null, boris.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId() + boris.getId(), boris.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test to id is null or not exists
    response = restTransferService.transfer(null, mike.getId(), null, BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId(), mike.getId() + boris.getId() + 1, BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test transfer to the same account
    response = restTransferService
        .transfer(null, mike.getId(), mike.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test amount is null or negative
    response = restTransferService.transfer(null, mike.getId(), boris.getId(), null);
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(-1));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test not enough money
    response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(200));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test transfer succeeded and check for money lose
    response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(50));
    assertEquals(response.getStatus(), OK.getStatusCode());
    assertEquals(mike.getBalance().add(boris.getBalance()),
        accountService.findById(mike.getId()).getBalance()
            .add(accountService.findById(boris.getId()).getBalance()));
    assertEquals(mike.getBalance().subtract(BigDecimal.valueOf(50)),
        accountService.findById(mike.getId()).getBalance());

    //check transaction saved
    TransactionLogService transactionLogService = injector.getInstance(TransactionLogService.class);
    List<TransactionLog> logList = transactionLogService.findByFromOrToId(mike.getId(), null, null);
    assertTrue(logList.size() == 1);

    //check transfer saved (transaction with the same externalId will not be repeated)
    response = restTransferService.transfer(1L, mike.getId(), boris.getId(), BigDecimal.valueOf(1));
    assertEquals(response.getStatus(), OK.getStatusCode());

    response = restTransferService.transfer(1L, mike.getId(), boris.getId(), BigDecimal.valueOf(1));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }
}
