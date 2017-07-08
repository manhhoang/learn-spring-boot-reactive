package com.spring_standalone;

import com.spring_standalone.config.AppConfig;
import com.spring_standalone.model.Album;
import com.spring_standalone.model.LastMusic;
import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import com.spring_standalone.service.LastMusicService;
import com.spring_standalone.service.TheMovieDBService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.concurrent.CompletableFuture;

import static com.spring_standalone.utils.Constants.IMDB;
import static com.spring_standalone.utils.Constants.LAST_MUSIC_SERVIC;
import static com.spring_standalone.utils.Constants.MUSIC;
import static com.spring_standalone.utils.Constants.THE_MOVIE_DB_SERVICE;


public class Application {

    public static void main(String[] args) throws Exception {
        String api = "imdb";
        String search = "Indiana Jones";
        execute(api, search);
    }

    private static void execute(String api, String search) throws Exception {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        if(api.equalsIgnoreCase(IMDB)) {
            TheMovieDBService theMovieDBService = (TheMovieDBService)ctx.getBean(THE_MOVIE_DB_SERVICE);
            CompletableFuture<TheMovieDB> theMovieDBFuture = theMovieDBService.search(search);
            TheMovieDB theMovieDB = theMovieDBFuture.get();
            for(Movie movie : theMovieDB.getMovies()) {
                System.out.println("Title: " + movie.getTitle() + " | Release Date: " + movie.getReleaseDate());
            }
        } else if(api.equalsIgnoreCase(MUSIC)){
            LastMusicService lastMusicService = (LastMusicService)ctx.getBean(LAST_MUSIC_SERVIC);
            CompletableFuture<LastMusic> lastMusicFuture = lastMusicService.search(search);
            LastMusic lastMusic = lastMusicFuture.get();
            for(Album album : lastMusic.getMusic().getAlbumMatches().getAlbums()) {
                System.out.println("Name: " + album.getName() + " | Artist: " + album.getArtist());
            }
        }
        ctx.close();
    }
}