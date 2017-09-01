package com.spring_standalone.service;

import com.spring_standalone.model.Movie;
import com.spring_standalone.model.TheMovieDB;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class TheMovieDBServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TheMovieDBService movieDBService;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(movieDBService);
    }

    @Test(expected=ExecutionException.class)
    public void testSearchException() throws ExecutionException, InterruptedException {
        String url = "nullnull";
        String movieName = "Indiana Jones";
        when(restTemplate.getForEntity(url, TheMovieDB.class, movieName)).thenReturn(new ResponseEntity(new TheMovieDB(), HttpStatus.OK));
        CompletableFuture<TheMovieDB> future = movieDBService.search(movieName);
        future.get();
    }

    @Test
    public void testSearchSuccess() throws ExecutionException, InterruptedException {
        String url = "nullnull";
        String movieName = "Indiana Jones";
        TheMovieDB theMovieDB = new TheMovieDB();
        Movie movie = new Movie();
        movie.setTitle("Indiana Jones Part 1");
        movie.setReleaseDate("5-12-1984");
        theMovieDB.addMovie(movie);
        when(restTemplate.getForEntity(url, TheMovieDB.class, movieName)).thenReturn(new ResponseEntity(theMovieDB, HttpStatus.OK));
        CompletableFuture<TheMovieDB> future = movieDBService.search(movieName);
        TheMovieDB result = future.get();
        Assert.assertEquals(result.getMovies().size(), 1);
        Assert.assertEquals(result.getMovies().get(0).getTitle(), "Indiana Jones Part 1");
        Assert.assertEquals(result.getMovies().get(0).getReleaseDate(), "5-12-1984");
    }

}
