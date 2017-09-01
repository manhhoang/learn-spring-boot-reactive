package com.synchronous.service;

import com.completablefuture.model.GitHubUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class GithubService {

    @Autowired
    private TestRestTemplate restTemplate;


    public GitHubUser find() {
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> entity = new HttpEntity<>(header);

        ResponseEntity<String> response = restTemplate.getForEntity("/api/users", String.class, entity);
        return null;
    }
}
