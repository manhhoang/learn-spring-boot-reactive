package com.spring_standalone;

import com.spring_standalone.config.AppConfig;
import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import com.spring_standalone.service.TheMovieDBService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CompletableFuture;

public class Application {

    public static void main(String[] args) throws Exception{
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(AppConfig.class);
        ctx.refresh();

        TheMovieDBService theMovieDBService = (TheMovieDBService)ctx.getBean("theMovieDBService");
        CompletableFuture<TheMovieDB> theMovieDBFuture = theMovieDBService.search("Indiana Jones");
        TheMovieDB theMovieDB = theMovieDBFuture.get();
        for(Movie movie : theMovieDB.getMovies()) {
            System.out.println("Title: " + movie.getTitle() + " | Release Date: " + movie.getReleaseDate());
        }
    }
}