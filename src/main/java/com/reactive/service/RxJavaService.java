package com.reactive.service;

import com.reactive.model.MessageAcknowledgement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.reactive.model.Message;
import rx.Observable;
import rx.Single;

import java.util.concurrent.TimeUnit;

@Service
public class RxJavaService {
	private static final Logger logger = LoggerFactory.getLogger(RxJavaService.class);

	public Single<MessageAcknowledgement> handleMessageSingle(Message message) {
		logger.info("About to Acknowledge");
		return Single.just(message)
				.delay(message.getDelayBy(), TimeUnit.MILLISECONDS)
				.flatMap(msg -> {
					if (msg.isThrowException()) {
						return Single.error(new IllegalStateException("Throwing a deliberate exception!"));
					}
					return Single.just(new MessageAcknowledgement(message.getId(), message.getPayload(), "From RxJavaService"));
				});
	}

	public Observable<MessageAcknowledgement> handleMessageObservable(Message message) {
		logger.info("About to Acknowledge");
		return Observable.just(message)
				.delay(message.getDelayBy(), TimeUnit.MILLISECONDS)
				.flatMap(msg -> {
					if (msg.isThrowException()) {
						return Observable.error(new IllegalStateException("Throwing a deliberate exception!"));
					}
					return Observable.just(new MessageAcknowledgement(message.getId(), message.getPayload(), "From RxJavaService"));
				});
	}

}
