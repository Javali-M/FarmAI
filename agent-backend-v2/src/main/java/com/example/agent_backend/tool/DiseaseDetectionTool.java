package com.example.agent_backend.tool;

import java.util.List;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.example.agent_backend.service.DiseaseDetectionService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DiseaseDetectionTool {

    private final DiseaseDetectionService diseaseDetectionService;

    public Mono<String> detectDisease(List<FilePart> images) {

        if (images.size() > 5) {
            return Mono.just("Too many images provided. Please provide up to 5 images of same plant.");
        }

        return diseaseDetectionService.detectDisease(images)
                .map(response -> response.toString());
    }
}