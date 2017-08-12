package com.completablefuture_jpa_swagger.service;

import com.completablefuture_jpa_swagger.model.Task;
import com.completablefuture_jpa_swagger.repository.CalculationRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class CalculationServiceImplTest {

    @Mock
    private CalculationRepository calculationRepository;

    @InjectMocks
    private CalculationServiceImpl calculationService;

    @Before
    public void setupMock() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMockCreation() {
        assertNotNull(calculationRepository);
        assertNotNull(calculationService);
    }

    @Test
    public void testStreamAll() {
        List<Task> tasks = new ArrayList<>();
        Task task = new Task();
        task.setTaskId("1");
        task.setDuration(10000);
        tasks.add(task);

        task = new Task();
        task.setTaskId("2");
        task.setDuration(20000);
        tasks.add(task);

        when(calculationRepository.streamAll()).thenReturn(tasks.stream());
        Stream<Task> taskStream = calculationRepository.streamAll();
        assertEquals(2, taskStream.count());
    }

    @Test
    public void testFindByTaskIdWithZeroTask() throws ExecutionException, InterruptedException {
        String taskId = "task_1";
        Optional<List<Task>> tasks = Optional.of(new ArrayList<>());
        when(calculationRepository.findByTaskId(taskId)).thenReturn(tasks);
        CompletableFuture<Task> futureTask = calculationService.findByTaskId(taskId);
        Task result = futureTask.get();
        assertEquals(result.getId(), 0);
        assertEquals(result.getTaskId(), "");
        assertEquals(result.getDuration(), 0);
    }

    @Test
    public void testFindByTaskIdWithOneTask() throws ExecutionException, InterruptedException {
        String taskId = "task_1";
        Optional<List<Task>> tasks = Optional.of(new ArrayList<>());
        Task task = new Task();
        task.setTaskId(taskId);
        task.setDuration(10000);
        tasks.get().add(task);
        when(calculationRepository.findByTaskId(taskId)).thenReturn(tasks);
        CompletableFuture<Task> futureTask = calculationService.findByTaskId(taskId);
        Task result = futureTask.get();
        assertEquals(result.getId(), 0);
        assertEquals(result.getTaskId(), taskId);
        assertEquals(result.getDuration(), 10000);
    }
}
