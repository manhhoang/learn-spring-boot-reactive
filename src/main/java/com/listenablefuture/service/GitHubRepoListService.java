package com.listenablefuture.service;

import com.listenablefuture.model.GitHubItems;
import com.listenablefuture.model.RepoListDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;

@Service
class GitHubRepoListService implements RepoListService {
    private static final String QUESTIONS_URL = "https://api.github.com/search/repositories?q={query}";

    @Autowired
    private AsyncRestTemplate asyncRestTemplate;

    @Override
    public ListenableFuture<RepoListDto> search(String query) {
        ListenableFuture<ResponseEntity<GitHubItems>> gitHubItems = asyncRestTemplate.getForEntity(QUESTIONS_URL, GitHubItems.class, query);
        return new RepositoryListDtoAdapter(query, gitHubItems);
    }
}
