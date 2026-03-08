package com.example.agent_backend.tool;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WeatherTool {

    private final WebClient webClient;

    public Mono<String> getWeather(String latitude, String longitude) {

        double lat = Double.parseDouble(latitude);
        double lon = Double.parseDouble(longitude);

        Mono<String> placeMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("nominatim.openstreetmap.org")
                        .path("/reverse")
                        .queryParam("lat", lat)
                        .queryParam("lon", lon)
                        .queryParam("format", "json")
                        .build())
                .header("User-Agent", "agent-backend/1.0")
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode address = json.path("address");
                    String city = address.path("city").asText(
                            address.path("town").asText(
                                    address.path("village").asText("Unknown")));
                    String state = address.path("state").asText("");
                    String country = address.path("country").asText("");
                    return "%s, %s, %s".formatted(city, state, country);
                })
                .onErrorReturn("Unknown location");

        Mono<JsonNode> weatherMono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.open-meteo.com")
                        .path("/v1/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current", "temperature_2m,relative_humidity_2m,surface_pressure,wind_speed_10m")
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> json.path("current"))
                .onErrorReturn(new ObjectMapper().createObjectNode());

        return Mono.zip(placeMono, weatherMono)
                .map(tuple -> {
                    String place = tuple.getT1();
                    JsonNode current = tuple.getT2();
                    return "The current weather at %s is: temperature %s°C, humidity %s%%, atmospheric pressure %s hPa, wind speed %s km/h."
                            .formatted(
                                    place,
                                    current.path("temperature_2m").asText("N/A"),
                                    current.path("relative_humidity_2m").asText("N/A"),
                                    current.path("surface_pressure").asText("N/A"),
                                    current.path("wind_speed_10m").asText("N/A"));
                })
                .onErrorReturn("Weather information is currently unavailable.");
    }

}
