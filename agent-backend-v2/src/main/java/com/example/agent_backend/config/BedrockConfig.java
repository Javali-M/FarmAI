package com.example.agent_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;

@Configuration
public class BedrockConfig {

    @Bean
    public BedrockAgentRuntimeClient bedrockAgentRuntimeClient() {

        return BedrockAgentRuntimeClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}
