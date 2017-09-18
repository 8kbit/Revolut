package com.revolut.rest;

import com.google.inject.Singleton;
import com.revolut.model.TransactionLog;
import com.revolut.service.TransactionLogService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

/**
 * Created by zaskanov on 09.09.2017.
 */
@Singleton
@Path("/v1/transactionLog/")
public class RESTTransactionLogService {
    @Inject
    private TransactionLogService transactionLogService;

    @GET
    @Produces(APPLICATION_JSON)
    public Response list(@QueryParam("accountId") Long accountId, @QueryParam("limit") @DefaultValue("100") Integer limit,
                         @QueryParam("offset") Integer offset) {
        limit = Math.min(limit, 100);
        List<TransactionLog> transactionLogs = transactionLogService.findByFromOrToId(accountId, limit, offset);
        return Response.ok().entity(transactionLogs).build();
    }
}