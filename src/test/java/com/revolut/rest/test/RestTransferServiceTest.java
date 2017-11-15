package com.revolut.rest.test;

import static javax.ws.rs.core.Response.Status.FORBIDDEN;
import static javax.ws.rs.core.Response.Status.OK;
import static org.testng.Assert.assertEquals;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.config.MyBatisModule;
import com.revolut.config.ServletConfig;
import com.revolut.config.TransferModule;
import com.revolut.model.Account;
import com.revolut.rest.RestTransferService;
import com.revolut.service.AccountService;
import com.revolut.service.TransactionLogService;
import java.io.IOException;
import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by zaskanov on 18.09.2017.
 */
@Test
public class RestTransferServiceTest {

  Injector injector;
  AccountService accountService;
  RestTransferService restTransferService;
  TransactionLogService transactionLogService;

  Account mike;
  Account boris;

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
    accountService = injector.getInstance(AccountService.class);
    restTransferService = injector.getInstance(RestTransferService.class);
    transactionLogService = injector.getInstance(TransactionLogService.class);

    mike = new Account("Mike", BigDecimal.valueOf(100));
    boris = new Account("Boris", BigDecimal.valueOf(100));

    accountService.save(mike);
    accountService.save(boris);
  }

  public void transfer_accountNotFound() throws IOException {
    Response response;

    //test FROM id is null or not exists
    response = restTransferService.transfer(null, null, boris.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId() + boris.getId(), boris.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    //test TO id is null or not exists
    response = restTransferService.transfer(null, mike.getId(), null, BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId(), mike.getId() + boris.getId() + 1, BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }

  public void transfer_sameAccount() throws IOException {
    //test transfer to the same account
    Response response = restTransferService
        .transfer(null, mike.getId(), mike.getId(), BigDecimal.valueOf(10));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }

  public void transfer_wrongAmountValue() throws IOException {
    Response response;

    //test amount is null or negative
    response = restTransferService.transfer(null, mike.getId(), boris.getId(), null);
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());

    response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(-1));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }

  public void transfer_notEnoughMoney() throws IOException {
    //test not enough money
    Response response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(200));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }

  public void transfer_logCheck() throws IOException {
    Response response;

    //check transfer saved
    response = restTransferService.transfer(1L, mike.getId(), boris.getId(), BigDecimal.valueOf(1));
    assertEquals(response.getStatus(), OK.getStatusCode());

    //check transaction with the same externalId will not be repeated
    response = restTransferService.transfer(1L, mike.getId(), boris.getId(), BigDecimal.valueOf(1));
    assertEquals(response.getStatus(), FORBIDDEN.getStatusCode());
  }

  public void transfer_success() throws IOException {
    //test transfer succeeded and check for money lose
    Response response = restTransferService
        .transfer(null, mike.getId(), boris.getId(), BigDecimal.valueOf(50));
    assertEquals(response.getStatus(), OK.getStatusCode());
    assertEquals(mike.getBalance().add(boris.getBalance()),
        accountService.findById(mike.getId()).getBalance()
            .add(accountService.findById(boris.getId()).getBalance()));
  }
}
