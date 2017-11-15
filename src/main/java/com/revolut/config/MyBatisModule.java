package com.revolut.config;

import org.mybatis.guice.XMLMyBatisModule;

/**
 * Created by zaskanov on 17.09.2017.
 */
public class MyBatisModule extends XMLMyBatisModule {

  @Override
  protected void initialize() {
    setClassPathResource("mybatis-config.xml");
  }
}
