package com.revolut.config;

import com.google.common.collect.ImmutableMap;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * Created by zaskanov on 09.09.2017.
 */
public class JerseyModule extends ServletModule {

  @Override
  protected void configureServlets() {
    ResourceConfig rc = new PackagesResourceConfig("com.revolut.rest");
    for (Class<?> resource : rc.getClasses()) {
      bind(resource);
    }

    serve("/services/*").with(GuiceContainer.class, ImmutableMap.of(
        JSONConfiguration.FEATURE_POJO_MAPPING, "true"
    ));
  }
}
