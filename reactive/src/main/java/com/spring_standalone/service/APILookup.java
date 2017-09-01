package com.spring_standalone.service;

import com.spring_standalone.config.AppConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static com.spring_standalone.utils.Constants.IMDB;
import static com.spring_standalone.utils.Constants.LAST_MUSIC_SERVIC;
import static com.spring_standalone.utils.Constants.MUSIC;
import static com.spring_standalone.utils.Constants.THE_MOVIE_DB_SERVICE;

public class APILookup {

    public ProviderService getService(APIType apiType) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        if(apiType.name().equals(IMDB)) {
            return (ProviderService)ctx.getBean(THE_MOVIE_DB_SERVICE);
        } else if(apiType.name().equals(MUSIC)){
            return (ProviderService)ctx.getBean(LAST_MUSIC_SERVIC);
        }
        ctx.close();
        return null;
    }

}
