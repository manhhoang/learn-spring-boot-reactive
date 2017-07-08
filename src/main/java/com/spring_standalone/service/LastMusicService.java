package com.spring_standalone.service;

import com.spring_standalone.exception.AppException;
import com.spring_standalone.model.Album;
import com.spring_standalone.model.AlbumMatches;
import com.spring_standalone.model.LastMusic;
import com.spring_standalone.model.Music;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;

@Service
public class LastMusicService {

    @Value("${lastmusic.key}")
    private String lastMusicKey;

    @Value("${lastmusic.url}")
    private String lastMusicUrl;

    private static final Logger logger = LoggerFactory.getLogger(LastMusicService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Cacheable("music")
    public CompletableFuture<LastMusic> search(String albumName) {
        final String url = lastMusicUrl + lastMusicKey;
        return CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(url, LastMusic.class, albumName))
                .thenApply(lastMusic -> {
                    LastMusic retLastMusic = new LastMusic();
                    Music music = new Music();
                    AlbumMatches albumMatches = new AlbumMatches();
                    int count = 1;
                    for(Album album: lastMusic.getBody().getMusic().getAlbumMatches().getAlbums()) {
                        if(count > 4)
                            break;
                        albumMatches.addAlbum(album);
                        count++;
                    }
                    music.setAlbumMatches(albumMatches);
                    retLastMusic.setMusic(music);
                    return retLastMusic;
                }).exceptionally(e -> {
                    logger.error("Failed to get movie from last music");
                    throw new AppException("100", "Failed to get movie from last music");
                });
    }
}
