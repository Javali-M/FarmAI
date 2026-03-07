package com.example.agent_backend.tool;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class EmailTool {

    public Mono<String> sendEmail(String to, String message) {

        System.out.println("Got sendEmail request for to=" + to + " message=" + message);

        return Mono.fromCallable(() -> {

            Thread.sleep(1000);
            return "Email sent to " + to + " with message: " + message;

        }).subscribeOn(Schedulers.boundedElastic());

    }

}
