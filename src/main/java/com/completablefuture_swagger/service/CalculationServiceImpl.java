package com.completablefuture_swagger.service;

import com.completablefuture_swagger.exception.ApiException;
import com.completablefuture_swagger.model.Task;
import com.completablefuture_swagger.repository.CalculationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.completablefuture_swagger.exception.ApiException.SAVE_ERROR_CODE;
import static com.completablefuture_swagger.exception.ApiException.SAVE_ERROR_MESSAGE;

@Service
public class CalculationServiceImpl implements CalculationService {

    private static final Logger logger = LoggerFactory.getLogger(CalculationServiceImpl.class);

    private final CalculationRepository calculationRepository;

    @Autowired
    public CalculationServiceImpl(CalculationRepository calculationRepository) {
        this.calculationRepository = calculationRepository;
    }

    @Override
    public CompletableFuture<Task> create(Task task) {
        return CompletableFuture.supplyAsync(() -> calculationRepository.save(task))
                .thenApply(this::handleCreateResponse);

    }

    private Task handleCreateResponse(Task task) {
        if(task == null) {
            logger.info("Failed creating task in CalculationService");
            throw new ApiException(SAVE_ERROR_CODE, SAVE_ERROR_MESSAGE);
        }
        return task;
    }

    @Override
    public CompletableFuture<Task> findByTaskId(String taskId) {
        return CompletableFuture.supplyAsync(() -> calculationRepository.findByTaskId(taskId))
                .thenApply(this::calculateAverageDuration);
    }

    private Task calculateAverageDuration(Optional<List<Task>> tasks) {
        long totalDuration = 0;
        String foundTaskId = "";
        for(Task task : tasks.get()) {
            totalDuration += task.getDuration();
            foundTaskId = task.getTaskId();
        }
        int size = tasks.get().size();
        long averageDuration = size != 0 ? totalDuration/size : 0;

        return new Task(foundTaskId, averageDuration);
    }
}
