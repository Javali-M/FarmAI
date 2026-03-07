package com.example.agent_backend.agent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

@Component
public class ToolParser {

    public Optional<ToolCall> parse(String response) {

        if (!response.contains("ACTION:")) {
            return Optional.empty();
        }

        String[] lines = response.split("\n");

        String toolName = null;
        Map<String,String> args = new HashMap<>();

        for (String line : lines) {

            line = line.trim();

            if (line.startsWith("ACTION:")) {
                toolName = line.replace("ACTION:", "").trim();
            }

            if (line.contains("=")) {

                String[] parts = line.split("=");

                if (parts.length == 2) {
                    args.put(parts[0].trim(), parts[1].trim());
                }
            }
        }

        if (toolName == null) {
            return Optional.empty();
        }

        return Optional.of(new ToolCall(toolName, args));
    }
}