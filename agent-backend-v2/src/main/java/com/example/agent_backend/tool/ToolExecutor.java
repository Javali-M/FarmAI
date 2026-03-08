package com.example.agent_backend.tool;

import java.util.List;
import java.util.Map;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class ToolExecutor {

    private final WeatherTool weatherTool;
    private final EmailTool emailTool;
    private final DiseaseDetectionTool diseaseDetectionTool;
    private final CropDiseaseKnowledgeTool knowledgeTool;
    private final MarketPriceTool marketPriceTool;

    public ToolExecutor(WeatherTool weatherTool,
            EmailTool emailTool, DiseaseDetectionTool diseaseDetectionTool,
            CropDiseaseKnowledgeTool knowledgeTool,
            MarketPriceTool marketPriceTool) {

        this.weatherTool = weatherTool;
        this.emailTool = emailTool;
        this.diseaseDetectionTool = diseaseDetectionTool;
        this.knowledgeTool = knowledgeTool;
        this.marketPriceTool = marketPriceTool;
    }

    public Mono<String> execute(String toolName, Map<String, String> args, List<FilePart> images) {

        switch (toolName) {

            case "getWeather":
                return weatherTool.getWeather(args.get("latitude"),
                        args.get("longitude"));

            case "sendEmail":
                return emailTool.sendEmail(
                        args.get("to"),
                        args.get("message"));

            case "detectDisease":
                if (images == null || images.isEmpty()) {
                    return Mono.just("No images provided for disease detection");
                }
                return diseaseDetectionTool.detectDisease(images);

            case "searchDiseaseKnowledge":

                return knowledgeTool.searchDiseaseKnowledge(
                        args.get("disease"));

            case "getBestMarket":
                return marketPriceTool.getBestMarket(
                        args.get("commodity"),
                        args.get("district"));

            default:
                return Mono.just("Unknown tool: " + toolName);
        }
    }
}