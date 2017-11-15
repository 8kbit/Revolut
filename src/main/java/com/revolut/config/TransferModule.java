package com.revolut.config;

import com.google.inject.servlet.ServletModule;
import com.revolut.rest.RestAccountService;
import com.revolut.rest.RestTransferService;
import com.revolut.service.AccountService;
import com.revolut.service.TransactionLogService;

/**
 * Created by zaskanov on 09.09.2017.
 */
public class TransferModule extends ServletModule {

  @Override
  protected void configureServlets() {
    bind(RestTransferService.class);
    bind(RestAccountService.class);
    bind(AccountService.class);
    bind(TransactionLogService.class);
  }
}
