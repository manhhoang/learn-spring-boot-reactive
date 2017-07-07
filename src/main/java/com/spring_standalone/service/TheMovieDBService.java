package com.spring_standalone.service;

import com.spring_standalone.exception.AppException;
import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class TheMovieDBService {

    private static final String THE_MOVIE_DB_URL =
            "https://api.themoviedb.org/3/search/movie?api_key=ea7ba32805fa642f35979db217a46d59&query={movieName}";

    private static final Logger logger = LoggerFactory.getLogger(TheMovieDBService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable("movies")
    public CompletableFuture<TheMovieDB> search(String movieName) {
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(THE_MOVIE_DB_URL, TheMovieDB.class, movieName))
                .thenApply(theMovieDB -> {
                    TheMovieDB retTheMovieDB = new TheMovieDB();
                    int count = 1;
                    for(Movie movie: theMovieDB.getBody().getMovies()){
                        if(count > 4)
                            break;
                        count++;
                        retTheMovieDB.addMovie(movie);
                    }
                    return retTheMovieDB;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from api.themoviedb.org");
                    throw new AppException("100", "Failed to get movie from api.themoviedb.org");
                });
    }
}
