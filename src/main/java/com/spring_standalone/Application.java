package com.spring_standalone;

import com.spring_standalone.model.Album;
import com.spring_standalone.model.LastMusic;
import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import com.spring_standalone.service.APIDelegate;
import com.spring_standalone.service.APILookup;
import com.spring_standalone.service.APIType;

import java.util.concurrent.CompletableFuture;

import static com.spring_standalone.utils.Constants.IMDB;
import static com.spring_standalone.utils.Constants.MUSIC;


public class Application {

    public static void main(String[] args) throws Exception {
        String api = "imdb";
        String search = "Indiana Jones";
        search(api, search);
    }

    private static void search(String api, String search) throws Exception {
        APIDelegate apiDelegate = new APIDelegate();
        apiDelegate.setLookupService(new APILookup());
        if(api.equalsIgnoreCase(IMDB)) {
            apiDelegate.setServiceType(APIType.IMDB);
            CompletableFuture<TheMovieDB> theMovieDBFuture = apiDelegate.doSearch(search);
            printTheMovieDB(theMovieDBFuture.get());
        } else if(api.equalsIgnoreCase(MUSIC)){
            apiDelegate.setServiceType(APIType.MUSIC);
            CompletableFuture<LastMusic> lastMusicFuture = apiDelegate.doSearch(search);
            printLastMusic(lastMusicFuture.get());
        }
    }

    private static void printTheMovieDB(TheMovieDB theMovieDB) {
        for(Movie movie : theMovieDB.getMovies()) {
            System.out.println("Title: " + movie.getTitle() + " | Release Date: " + movie.getReleaseDate());
        }
    }

    private static void printLastMusic(LastMusic lastMusic) {
        for(Album album : lastMusic.getMusic().getAlbumMatches().getAlbums()) {
            System.out.println("Name: " + album.getName() + " | Artist: " + album.getArtist());
        }
    }
}