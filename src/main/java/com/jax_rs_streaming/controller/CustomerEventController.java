package com.jax_rs_streaming.controller;

import com.jax_rs_streaming.model.CustomerEvent;
import com.jax_rs_streaming.repository.CustomerEventRepositoryAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Path("/events")
public class CustomerEventController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerEventController.class);

    private CustomerEventRepositoryAsync customerEventRepositoryAsync;

    public CustomerEventController(CustomerEventRepositoryAsync customerEventRepositoryAsync) {
        this.customerEventRepositoryAsync = customerEventRepositoryAsync;
    }

    @GET
    @Path("/stream")
    public Response streamEvents() {
        final StreamingOutput streamingOutput = outputStream -> {
            final Observable<CustomerEvent> allEvents = customerEventRepositoryAsync.getCustomerEventsObservable();
            final CountDownLatch countDownLatch = new CountDownLatch(1);

            LOGGER.info("Given output stream: " + outputStream);

            allEvents.subscribe(new Observer<CustomerEvent>() {
                @Override
                public void onCompleted() {
                    try {
                        LOGGER.info("Finished writing out events");
                        outputStream.close();
                    } catch (IOException e) {
                        LOGGER.warn("Exception closing output stream", e);
                    } finally {
                        countDownLatch.countDown();
                    }
                }
                @Override
                public void onError(Throwable e) {
                    LOGGER.error("Error streaming events");
                }
                @Override
                public void onNext(CustomerEvent customerEvent) {
                    try {
                        outputStream.write(customerEvent.serialise());
                    } catch (IOException e) {
                        LOGGER.warn("Exception ", e);
                    }
                }
            });

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                LOGGER.warn("Current thread interrupted, resetting flag");
                Thread.currentThread().interrupt();
            }
        };

        return Response.ok().entity(streamingOutput).build();
    }

}
