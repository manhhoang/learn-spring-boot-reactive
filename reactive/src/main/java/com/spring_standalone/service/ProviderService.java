package com.spring_standalone.service;

import java.util.concurrent.CompletableFuture;

public interface ProviderService<T> {

    CompletableFuture<T> search(String name);
}
