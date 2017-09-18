package com.revolut.rest;

import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.revolut.model.Account;
import com.revolut.model.TransactionLog;
import com.revolut.rest.helper.Msg;
import com.revolut.service.AccountService;
import com.revolut.service.TransactionLogService;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.Objects;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.FORBIDDEN;

/**
 * Created by zaskanov on 09.09.2017.
 */
@Singleton
@Path("/v1/transfer/")
public class RESTTransferServiceImpl implements RESTTransferService {

    @Inject
    private AccountService accountService;

    @Inject
    private TransactionLogService transactionLogService;

    @POST
    @Produces(APPLICATION_JSON)
    @Transactional(rollbackOn = Exception.class)
    public Response transfer(@QueryParam("from") Long fromId, @QueryParam("to") Long toId, @QueryParam("amount") BigDecimal amount) {
        if (Objects.equals(fromId, toId))
            return Response.status(FORBIDDEN).entity(new Msg("account is the same")).build();
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            return Response.status(FORBIDDEN).entity(new Msg("amount should be positive")).build();

        Account from = accountService.findById(fromId);
        Account to = accountService.findById(toId);

        if (from == null) return Response.status(FORBIDDEN).entity(new Msg("from account not found")).build();
        if (to == null) return Response.status(FORBIDDEN).entity(new Msg("to account not found")).build();
        if (from.getBalance().compareTo(amount) < 0)
            return Response.status(FORBIDDEN).entity(new Msg("balance too low")).build();

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
        accountService.update(from);
        accountService.update(to);
        transactionLogService.save(new TransactionLog(from, to, amount));

        return Response.ok().build();
    }
}