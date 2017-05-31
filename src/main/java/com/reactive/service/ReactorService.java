package com.reactive.service;

import com.reactive.model.MessageAcknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.reactive.model.Message;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;

@Service
public class ReactorService {
    private static final Logger logger = LoggerFactory.getLogger(ReactorService.class);

    public Mono<MessageAcknowledgement> handleMessageMono(Message message) {
        return Mono
                .delay(Duration.ofMillis(message.getDelayBy()))
                .then(Mono.just(message))
                .map(msg -> Tuples.of(msg, msg.isThrowException()))
                .flatMap(tup -> {
                    if (tup.getT2()) {
                        return Mono.error(new RuntimeException("Throwing a deliberate Exception!"));
                    }
                    Message msg = tup.getT1();
                    return Mono.just(new MessageAcknowledgement(msg.getId(), msg.getPayload(), "Response from ReactorService"));
                }).single();
    }

    public Flux<MessageAcknowledgement> handleMessageFlux(Message message) {
        return Flux.just(message)
                .delay(Duration.ofMillis(message.getDelayBy()))
                .map(msg -> Tuples.of(msg, msg.isThrowException()))
                .flatMap(tup -> {
                    if (tup.getT2()) {
                        return Flux.error(new RuntimeException("Throwing a deliberate Exception!"));
                    }
                    Message msg = tup.getT1();
                    return Flux.just(new MessageAcknowledgement(msg.getId(), msg.getPayload(), "Response from ReactorService"));
                });
    }

}
