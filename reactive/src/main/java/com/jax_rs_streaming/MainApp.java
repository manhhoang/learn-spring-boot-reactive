package com.jax_rs_streaming;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.jax_rs_streaming.config.ApplicationConfiguration;
import com.jax_rs_streaming.controller.CustomerEventController;
import com.jax_rs_streaming.repository.CustomerEventRepositoryAsync;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MainApp extends Application<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new MainApp().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {

    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {
        Cluster cluster = applicationConfiguration.getCassandraFactory().build(environment);
        Session connect = cluster.connect();
        CustomerEventRepositoryAsync customerEventRepositoryAsync = new CustomerEventRepositoryAsync(connect);
        CustomerEventController customerEventController = new CustomerEventController(customerEventRepositoryAsync);
        environment.jersey().register(customerEventController);
    }
}