package com.revolut.config;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.revolut.model.Account;
import com.revolut.service.AccountService;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;

/**
 * Created by zaskanov on 09.09.2017.
 */
public class ServletConfig extends GuiceServletContextListener {

    @Override
    protected Injector getInjector() {
        Injector injector = Guice.createInjector(new TransferModule(), new MyBatisModule());

        migrateDB(injector);
        populateDB(injector);

        return injector;
    }

    private void migrateDB(Injector injector) {
        try {
            // prepare the test db
            Environment environment = injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
            DataSource dataSource = environment.getDataSource();
            ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
            runner.setAutoCommit(true);
            runner.setStopOnError(true);
            runner.runScript(Resources.getResourceAsReader("db/database-schema.sql"));
            runner.closeConnection();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void populateDB(Injector injector) {
        AccountService accountService = injector.getInstance(AccountService.class);

        Account mike = new Account("Mike", BigDecimal.valueOf(100));
        Account boris = new Account("Boris", BigDecimal.valueOf(100));

        accountService.save(mike);
        accountService.save(boris);

        int a = 8;
    }
}