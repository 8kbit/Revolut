package com.revolut.model;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zaskanov on 09.09.2017.
 */
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class TransactionLog {
    private Long id;
    private Account from;
    private Account to;
    private BigDecimal amount;
    private Date created;

    public TransactionLog(Long id, BigDecimal amount, Timestamp timestamp) {
        this.id = id;
        this.amount = amount;
        this.created = new Date(timestamp.getTime());
    }

    public TransactionLog(Account from, Account to, BigDecimal amount) {
        this.from = from;
        this.to = to;
        this.amount = amount;
    }
}
