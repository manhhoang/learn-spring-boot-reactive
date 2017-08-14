package com.completablefuture_jpa_streams_swagger.service;

import com.completablefuture_jpa_streams_swagger.exception.ApiException;
import com.completablefuture_jpa_streams_swagger.model.Task;
import com.completablefuture_jpa_streams_swagger.repository.CalculationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static com.completablefuture_jpa_streams_swagger.exception.ApiException.SAVE_ERROR_CODE;
import static com.completablefuture_jpa_streams_swagger.exception.ApiException.SAVE_ERROR_MESSAGE;

@Service
public class CalculationServiceImpl implements CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);

    private final CalculationRepository calculationRepository;

    @Autowired
    public CalculationServiceImpl(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Override
    public Stream<Task> streamAll() {
        return calculationRepository.streamAll();
    }

    @Override
    public CompletableFuture<Task> create(Task task) {
        return CompletableFuture.supplyAsync(() -> calculationRepository.save(task))
                .thenApply(this::handleCreateResponse);

    }

    private Task handleCreateResponse(Task task) {
        if (task == null) {
            logger.info("Failed creating task in CalculationService");
            throw new ApiException(SAVE_ERROR_CODE, SAVE_ERROR_MESSAGE);
        }
        return task;
    }

    @Override
    public CompletableFuture<Task> findByTaskId(String taskId) {
        return CompletableFuture.supplyAsync(() -> calculationRepository.findTaskAverageDuration(taskId))
                .thenApply(average -> {
                    if (average == null)
                        return new Task("", 0d);
                    else
                        return new Task(taskId, average.orElse(0d));
                }).exceptionally(e -> {
                    logger.info("Failed to find task in CalculationService");
                    throw new ApiException("200", "Failed to find task in CalculationService");
                });
    }
}
