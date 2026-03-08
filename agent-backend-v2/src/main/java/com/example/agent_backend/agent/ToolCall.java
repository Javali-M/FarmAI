package com.example.agent_backend.agent;

import java.util.Map;

public class ToolCall {

    private String toolName;
    private Map<String,String> args;

    public ToolCall(String toolName, Map<String,String> args) {
        this.toolName = toolName;
        this.args = args;
    }

    public String getToolName() {
        return toolName;
    }

    public Map<String,String> getArgs() {
        return args;
    }
}
