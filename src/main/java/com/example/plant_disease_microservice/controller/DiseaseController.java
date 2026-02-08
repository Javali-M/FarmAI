package com.example.plant_disease_microservice.controller;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.plant_disease_microservice.service.DiseaseDetectionService;
import com.example.plant_disease_microservice.models.DiseaseResponse;
import java.util.List;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/disease")
public class DiseaseController {

    private final DiseaseDetectionService service;

    public DiseaseController(DiseaseDetectionService service) {
        this.service = service;
    }

    @PostMapping(value = "/detect", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<DiseaseResponse> detect(
            @RequestPart("images") List<FilePart> images) throws IllegalArgumentException {

        if (images == null || images.isEmpty() || images.size() > 5) {
            throw new IllegalArgumentException("Invalid number of images provided");
        }

        return service.detectDisease(images);
    }
}
