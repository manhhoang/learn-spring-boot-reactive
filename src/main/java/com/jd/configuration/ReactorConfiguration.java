package com.jd.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;

@Configuration
public class ReactorConfiguration {
    static {
        Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    public EventBus eventBus() {
        return EventBus.config().env(Environment.get()).dispatcher(Environment.SHARED).get();
    }
}
