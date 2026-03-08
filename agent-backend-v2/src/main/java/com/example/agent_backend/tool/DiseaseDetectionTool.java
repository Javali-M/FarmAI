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

        return diseaseDetectionService.detectDisease(images)
                .map(response -> response.toString());
    }
}