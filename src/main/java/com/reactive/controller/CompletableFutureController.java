package com.reactive.controller;


import com.reactive.model.Message;
import com.reactive.model.MessageAcknowledgement;
import com.reactive.service.CompletableFutureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class CompletableFutureController {

	private final CompletableFutureService aService;

	@Autowired
	public CompletableFutureController(CompletableFutureService aService) {
		this.aService = aService;
	}

	@RequestMapping(path = "/handleMessageFuture", method = RequestMethod.POST)
	public CompletableFuture<MessageAcknowledgement> handleMessage(@RequestBody Message message) {
		return this.aService.handleMessage(message);
	}

}
