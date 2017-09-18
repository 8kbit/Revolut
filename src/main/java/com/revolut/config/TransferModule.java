package com.revolut.config;

import com.google.common.collect.ImmutableMap;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.revolut.rest.RESTAccountService;
import com.revolut.rest.RESTTransferService;
import com.revolut.rest.RESTTransferServiceImpl;
import com.revolut.service.AccountService;
import com.revolut.service.TransactionLogService;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by zaskanov on 09.09.2017.
 */
public class TransferModule extends ServletModule {
    @Override
    protected void configureServlets() {
        bind(RESTTransferService.class).to(RESTTransferServiceImpl.class);
//        bind(RESTTransferService.class).to(RESTTransferServiceImpl.class);
        bind(RESTAccountService.class);
        bind(AccountService.class);
        bind(TransactionLogService.class);

        ResourceConfig rc = new PackagesResourceConfig("com.revolut.rest");
        for (Class<?> resource : rc.getClasses()) {
            bind(resource);
        }

        loadProperties();

        serve("/services/*").with(GuiceContainer.class, ImmutableMap.of(
                JSONConfiguration.FEATURE_POJO_MAPPING, "true"
        ));
    }

    private void loadProperties() {
        InputStream stream = this.getClass().getResourceAsStream("/dev.properties");
        Properties appProperties = new Properties();
        try {
            appProperties.load(stream);
            Names.bindProperties(binder(), appProperties);
        } catch (IOException e) {
            // Это предпочтительный способ сообщения Guice о том, что что-то пошло не так
            binder().addError(e);
        }
    }
}
