package com.revolut.rest;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

/**
 * Created by zaskanov on 09.09.2017.
 */
public interface RESTTransferService {
    Response transfer(Long fromId, Long toId, BigDecimal amount);
}