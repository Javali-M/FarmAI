package com.example.agent_backend.tool;

import java.util.List;
import java.util.Map;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;

import com.example.agent_backend.security.RequestContext;

import reactor.core.publisher.Mono;

@Component
public class ToolExecutor {

    private final WeatherTool weatherTool;
    private final EmailTool emailTool;
    private final DiseaseDetectionTool diseaseDetectionTool;
    private final CropDiseaseKnowledgeTool knowledgeTool;
    private final MarketPriceTool marketPriceTool;
    private final RequestContext requestContext;

    public ToolExecutor(WeatherTool weatherTool,
            EmailTool emailTool, DiseaseDetectionTool diseaseDetectionTool,
            CropDiseaseKnowledgeTool knowledgeTool,
            MarketPriceTool marketPriceTool,
            RequestContext requestContext) {

        this.weatherTool = weatherTool;
        this.emailTool = emailTool;
        this.diseaseDetectionTool = diseaseDetectionTool;
        this.knowledgeTool = knowledgeTool;
        this.marketPriceTool = marketPriceTool;
        this.requestContext = requestContext;
    }

    public Mono<String> execute(String toolName, Map<String, String> args, List<FilePart> images) {

        switch (toolName) {

            case "getWeather":
                if (args.containsKey("latitude") && args.containsKey("longitude") && !args.get("latitude").isBlank()
                        && !args.get("longitude").isBlank()) {
                    return weatherTool.getWeather(args.get("latitude"),
                            args.get("longitude"));
                }
                // fallback to user's location from JWT if not provided in args
                if (requestContext.getLatitude() != null && requestContext.getLongitude() != null) {
                    // System.out.println("Using location from JWT - lat: " + requestContext.getLatitude() + ", lng: "
                    //         + requestContext.getLongitude());
                    return weatherTool.getWeather(
                            String.valueOf(requestContext.getLatitude()),
                            String.valueOf(requestContext.getLongitude()));
                }
                return Mono.just("Location not allowed during login/signup. Please allow location acces and try again");

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