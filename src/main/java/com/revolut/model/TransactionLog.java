package com.revolut.model;

import com.revolut.helper.CustomDateSerializer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zaskanov on 09.09.2017.
 */
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Getter
@Setter
public class TransactionLog {
    private Long id;
    private Long externalId;
    @JsonIgnore
    private Account from;
    @JsonIgnore
    private Account to;
    private BigDecimal amount;

    @JsonSerialize(using = CustomDateSerializer.class)
    private Date created;

    public TransactionLog(Long id, Long externalId, BigDecimal amount, Timestamp timestamp) {
        this.id = id;
        this.externalId = externalId;
        this.amount = amount;
        this.created = new Date(timestamp.getTime());
    }

    public TransactionLog(Long externalId, Account from, Account to, BigDecimal amount) {
        this.externalId = externalId;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }


}
