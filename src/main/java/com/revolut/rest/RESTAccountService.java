package com.revolut.rest;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import com.google.inject.Singleton;
import com.revolut.model.Account;
import com.revolut.service.AccountService;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 * Created by zaskanov on 09.09.2017.
 */
@Singleton
@Path("/v1/account/")
public class RESTAccountService {

  @Inject
  private AccountService accountService;

  @GET
  @Produces(APPLICATION_JSON)
  public Response list(@QueryParam("limit") @DefaultValue("100") Integer limit,
      @QueryParam("offset") Integer offset) {
    limit = Math.min(limit, 100);
    List<Account> accountList = accountService.list(limit, offset);
    return Response.ok().entity(accountList).build();
  }
}