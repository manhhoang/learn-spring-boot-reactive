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
public class LastMusicService {

    private static final String LAST_MUSIC_URL =
            "http://ws.audioscrobbler.com/2.0/?method=album.search&album={musicName}&api_key=a4444f25ea835fd13aa26fea082b6831&format=json";

    //http://ws.audioscrobbler.com/2.0/?method=album.search&album=believe&api_key=a4444f25ea835fd13aa26fea082b6831&format=json
    private static final Logger logger = LoggerFactory.getLogger(LastMusicService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable("music")
    public CompletableFuture<TheMovieDB> search(String albumName) {
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(LAST_MUSIC_URL, TheMovieDB.class, albumName))
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
