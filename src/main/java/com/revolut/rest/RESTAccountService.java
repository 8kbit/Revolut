package com.revolut.rest;

import com.google.inject.Singleton;
import com.revolut.model.Account;
import com.revolut.service.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

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
    public Response list(@QueryParam("limit") @DefaultValue("100") Integer limit, @QueryParam("offset") Integer offset) {
        limit = Math.max(limit, 100);
        List<Account> accountList = accountService.list(limit, offset);
        return Response.ok().entity(accountList).build();
    }
}