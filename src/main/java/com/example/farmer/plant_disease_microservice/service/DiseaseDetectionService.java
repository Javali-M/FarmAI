package com.example.farmer.plant_disease_microservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.farmer.plant_disease_microservice.models.DiseaseResponse;
import com.example.farmer.plant_disease_microservice.models.PlantNetApiResponse;

import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class DiseaseDetectionService {

    @Value("${plantnet.api.key}")
    private String API_KEY;

    private final WebClient webClient;

    public DiseaseDetectionService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<DiseaseResponse> detectDisease(List<FilePart> images) {

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();

        for (FilePart file : images) {
            bodyBuilder.part("images", file)
                    .filename(file.filename())
                    .contentType(MediaType.IMAGE_JPEG);
        }

        bodyBuilder.part("organs", "auto");

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host("my-api.plantnet.org")
                        .path("/v2/diseases/identify")
                        .queryParam("api-key", API_KEY)
                        .queryParam("include-related-images", false)
                        .queryParam("no-reject", false)
                        .queryParam("nb-results", 1)
                        .queryParam("lang", "en")
                        .build())
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(PlantNetApiResponse.class)
                .map(this::toFinalResponse);
    }

    public DiseaseResponse toFinalResponse(PlantNetApiResponse apiResponse) {
        if (apiResponse.results == null || apiResponse.results.isEmpty()) {
            System.out.println("No disease detected");
            return new DiseaseResponse("No disease detected", "N/A", 0.0);
        }

        PlantNetApiResponse.Result result = apiResponse.results.get(0);

        System.out.println("Disease Name: " + result.name);
        System.out.println("Description: " + result.description);
        System.out.println("Confidence: " + result.score);

        return new DiseaseResponse(result.name, result.description, result.score);
    }

}
