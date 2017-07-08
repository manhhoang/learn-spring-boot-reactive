package com.spring_standalone.service;

import com.spring_standalone.exception.AppException;
import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class TheMovieDBService {

    @Value("${themoviedb.key}")
    private String theMovieDbKey;

    @Value("${themoviedb.url}")
    private String theMovieDbUrl;

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger logger = LoggerFactory.getLogger(TheMovieDBService.class);

    @Cacheable("movie")
    public CompletableFuture<TheMovieDB> search(String movieName) {
        final String url = theMovieDbUrl + theMovieDbKey;
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, TheMovieDB.class, movieName))
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
