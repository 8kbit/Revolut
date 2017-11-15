package com.revolut.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by zaskanov on 09.09.2017.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class Account {

  private Long id;
  private String holderName;
  private BigDecimal balance;
  private Long version;

  public Account(Long id, String holderName, BigDecimal balance) {
    this.id = id;
    this.holderName = holderName;
    this.balance = balance;
    this.version = 0L;
  }

  public Account(String holderName, BigDecimal balance) {
    this.holderName = holderName;
    this.balance = balance;
    this.version = 0L;
  }
}
