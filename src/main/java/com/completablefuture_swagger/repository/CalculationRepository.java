package com.completablefuture_swagger.repository;

import com.completablefuture_swagger.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CalculationRepository extends CrudRepository<Task, Long> {

    Optional<List<Task>> findByTaskId(String taskId);
}
