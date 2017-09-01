package com.listenablefuture.service;

import com.listenablefuture.model.RepoListDto;
import org.springframework.util.concurrent.ListenableFuture;

public interface RepoListService {

    ListenableFuture<RepoListDto> search(String query);
}
