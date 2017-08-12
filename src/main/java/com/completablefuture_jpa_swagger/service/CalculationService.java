package com.completablefuture_jpa_swagger.service;

import com.completablefuture_jpa_swagger.model.Task;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

public interface CalculationService {

    Stream<Task> streamAll();

    /**
     * Save the task to database
     * @param task
     * @return CompletableFuture<Task>
     */
    CompletableFuture<Task> create(Task task);

    /**
     * Find the task by task Id
     * @param taskId
     * @return CompletableFuture<Task>
     */
    CompletableFuture<Task> findByTaskId(String taskId);
}
