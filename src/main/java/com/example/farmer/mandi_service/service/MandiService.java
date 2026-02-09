package com.example.farmer.mandi_service.service;

import com.example.farmer.mandi_service.models.MandiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MandiService {

    @Value("${mandi.api.key}")
    private String API_KEY;

    private final WebClient webClient;

    public MandiService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<List<MandiResponse>> getMandiPrices(
            String state,
            String commodity) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("api.data.gov.in")
                        .path("/resource/35985678-0d79-46b4-9ed6-6f13308a1d24")
                        .queryParam("api-key", API_KEY)
                        .queryParam("format", "json")
//                        .queryParam("limit", 10)
                        .queryParam("filters[State]", state)
                        .queryParam("filters[Commodity]", commodity)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(MandiApiResponse.class)
                .map(this::toFinalResponse);
    }

    private List<MandiResponse> toFinalResponse(
            MandiApiResponse apiResponse) {

        if (apiResponse.records == null ||
                apiResponse.records.isEmpty()) {

            System.out.println("No mandi price data found");
            return List.of();
        }

        return apiResponse.records.stream()
                .map(r -> new MandiResponse(
                        r.Market,
                        r.Commodity,
                        Double.parseDouble(r.Modal_Price),
                        r.State,
                        r.District
                ))

                .collect(Collectors.toList());
    }

    // ===== INNER CLASS FOR API RESPONSE =====

    public static class MandiApiResponse {
        public List<Record> records;

        public static class Record {
            public String State;
            public String District;
            public String Market;
            public String Commodity;
            public String Modal_Price;
        }
    }
}
