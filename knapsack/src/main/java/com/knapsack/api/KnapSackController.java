package com.knapsack.api;

import com.knapsack.model.Item;
import com.knapsack.service.KnapSackService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api")
public class KnapSackController {

    private final KnapSackService knapSackService;

    @Autowired
    public KnapSackController(KnapSackService knapSackService) {
        this.knapSackService = knapSackService;
    }

    @ApiOperation(value = "Optimizing.", notes = "", response = Item.class, tags = {"Optimizing"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Item.class),
            @ApiResponse(code = 400, message = "Request failed.", response = Item.class),
            @ApiResponse(code = 401, message = "Authorization has been denied for this request.", response = Item.class),
            @ApiResponse(code = 422, message = "Model validation failed.", response = Item.class),
            @ApiResponse(code = 429, message = "Too many requests.", response = Item.class),
            @ApiResponse(code = 500, message = "Internal server error.", response = Item.class)})
    @RequestMapping(value = "/v1/knapsack/{capacity}", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public CompletableFuture<List<Item>> optimizing(@PathVariable("capacity") String capacity, @RequestBody List<Item> items) {
        return CompletableFuture.completedFuture(knapSackService.optimizing(Integer.parseInt(capacity), items));
    }
}
