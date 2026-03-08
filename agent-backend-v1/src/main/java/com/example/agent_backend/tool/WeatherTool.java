package com.example.agent_backend.tool;

import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
public class WeatherTool {

    public Mono<String> getWeather(String city) {

        System.out.println("Got weather call API for city=" + city);

        return Mono.fromCallable(() -> {

            Thread.sleep(1000);
            return "Weather in " + city + " is 30°C and sunny";

        }).subscribeOn(Schedulers.boundedElastic());

    }

}
