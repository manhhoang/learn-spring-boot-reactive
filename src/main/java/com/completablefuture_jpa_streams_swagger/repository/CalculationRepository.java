package com.completablefuture_jpa_streams_swagger.repository;

import com.completablefuture_jpa_streams_swagger.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface CalculationRepository extends CrudRepository<Task, Long> {

    @Query("select t from Task t")
    Stream<Task> streamAll();

    Optional<List<Task>> findByTaskId(String taskId);

    @Query("select AVG(t.duration) from Task t where t.taskId = ?#{[0]} GROUP By t.taskId")
    Optional<Double> findTaskAverageDuration(String taskId);
}
