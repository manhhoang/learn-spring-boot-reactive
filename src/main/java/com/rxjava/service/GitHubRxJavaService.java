package com.rxjava.service;

import com.completablefuture.model.GitHubUser;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.Future;

@Service
public class GitHubRxJavaService {

    private final WebTarget target = ClientBuilder.newClient().target("https://api.github.com/");

    public Future<GitHubUser> userAsync(String user) {
        return target
                .path("/users/{user}")
                .resolveTemplate("user", user)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .async()
                .get(GitHubUser.class);
    }
}
