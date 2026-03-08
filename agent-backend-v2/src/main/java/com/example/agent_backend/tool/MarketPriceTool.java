package com.example.agent_backend.tool;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketPriceTool {

    private final WebClient webClient;

    @Value("${datagov.api.key}")
    private String apiKey;

    private static final String HOST = "api.data.gov.in";
    private static final String PATH = "/resource/35985678-0d79-46b4-9ed6-6f13308a1d24";

    public Mono<String> getBestMarket(String commodity, String district) {

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .host(HOST)
                        .path(PATH)
                        .queryParam("api-key", apiKey)
                        .queryParam("format", "json")
                        .queryParam("limit", "10")
                        .queryParam("filters[commodity]", commodity)
                        .queryParam("filters[district]", district)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode records = json.path("records");

                    if (records.isEmpty()) {
                        return "No market data found for %s in %s district.".formatted(commodity, district);
                    }

                    List<JsonNode> markets = new ArrayList<>();
                    records.forEach(markets::add);

                    
                    markets.sort(Comparator.comparingDouble(
                            n -> -parsePrice(n.path("modal_price").asText("0"))
                    ));

                    JsonNode best = markets.get(0);
                    String marketName  = best.path("market").asText("N/A");
                    String state       = best.path("state").asText("N/A");
                    String minPrice    = best.path("min_price").asText("N/A");
                    String maxPrice    = best.path("max_price").asText("N/A");
                    String modalPrice  = best.path("modal_price").asText("N/A");
                    String arrivalDate = best.path("arrival_date").asText("N/A");

                    // top 3 alternatives for context
                    StringBuilder sb = new StringBuilder();
                    sb.append("The best market to sell %s in %s district is %s (%s). ".formatted(commodity, district, marketName, state));
                    sb.append("As of %s, modal price is ₹%s/quintal (min: ₹%s, max: ₹%s). ".formatted(arrivalDate, modalPrice, minPrice, maxPrice));

                    if (markets.size() > 1) {
                        sb.append("Other nearby options: ");
                        markets.stream().skip(1).limit(2).forEach(m ->
                                sb.append("%s at ₹%s modal, ".formatted(
                                        m.path("market").asText("N/A"),
                                        m.path("modal_price").asText("N/A")))
                        );
                    }

                    return sb.toString().trim().replaceAll(", $", ".");
                })
                .onErrorReturn("Market price data for %s in %s is currently unavailable.".formatted(commodity, district));
    }

    private double parsePrice(String price) {
        try {
            return Double.parseDouble(price.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}