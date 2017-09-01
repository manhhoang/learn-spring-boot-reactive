package com.completablefuture_jpa_streams_swagger.controller;

import com.completablefuture_jpa_streams_swagger.model.Task;
import com.completablefuture_jpa_streams_swagger.service.CalculationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.CompletableFuture;

@Controller
@RequestMapping("/api")
public class CalculationController {

    private final CalculationService calculationService;

    @Autowired
    public CalculationController(CalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @ApiOperation(value = "Create task.", notes = "", response = Task.class, tags = {"Task",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Task.class),
            @ApiResponse(code = 400, message = "Request failed.", response = Task.class),
            @ApiResponse(code = 401, message = "Authorization has been denied for this request.", response = Task.class),
            @ApiResponse(code = 422, message = "Model validation failed.", response = Task.class),
            @ApiResponse(code = 429, message = "Too many requests.", response = Task.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = Task.class)})
    @RequestMapping(value = "/v1/task", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public CompletableFuture<Task> createTask(@RequestBody Task task) {
        return this.calculationService.create(task);
    }

    @ApiOperation(value = "Get task.", notes = "", response = Task.class, tags = {"Task",})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Task.class),
            @ApiResponse(code = 400, message = "Request failed.", response = Task.class),
            @ApiResponse(code = 401, message = "Authorization has been denied for this request.", response = Task.class),
            @ApiResponse(code = 422, message = "Model validation failed.", response = Task.class),
            @ApiResponse(code = 429, message = "Too many requests.", response = Task.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = Task.class)})
    @RequestMapping(value = "/v1/task/{taskId}", method = RequestMethod.GET, headers = "Accept=application/json")
    @ResponseBody
    public CompletableFuture<Task> getTask(@PathVariable("taskId") String taskId) {
        return this.calculationService.findByTaskId(taskId);
    }

}
