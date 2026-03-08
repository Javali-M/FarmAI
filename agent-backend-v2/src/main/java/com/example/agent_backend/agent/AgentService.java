package com.example.agent_backend.agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.example.agent_backend.tool.ToolExecutor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AgentService {

    private final ChatClient chatClient;
    private final ToolParser toolParser;
    private final ToolExecutor toolExecutor;

    private static final int MAX_STEPS = 6;

    public AgentService(ChatClient.Builder builder,
            ToolParser toolParser,
            ToolExecutor toolExecutor) {

        this.chatClient = builder.build();
        this.toolParser = toolParser;
        this.toolExecutor = toolExecutor;
    }

    public Flux<String> runAgent(String query, List<FilePart> images) {

        List<Message> messages = new ArrayList<>();

        messages.add(new SystemMessage("""
                        You are an AI agent.

                        Available tools:

                        getWeather(latitude, longitude)
                        sendEmail(to,message)
                        detectDisease(images)
                        getBestMarket(commodity, district)
                        searchDiseaseKnowledge(disease)

                        Rules:
                        - if the user is asking for mandi price or best market to sell, use getBestMarket with appropriate parameters.
                        - If the current user message contains images, they are ALREADY provided to you.
                        NEVER ask the user to upload or re-send images. Use them immediately.
                        - If the user uploads an image and asks about disease,
                        first call detectDisease, then call searchDiseaseKnowledge.
                        - If the user asks about cause, symptoms, treatment, or prevention
                        of any crop disease, use searchDiseaseKnowledge.
                        - If the user asks about crop disease or plant infection
                        AND images are present in this message, call detectDisease immediately.
                        - If no images are present and the task requires them, then and only then
                        ask the user to provide an image.
                        - Use your words to formulate the query for the tool. Do not just copy-paste user query as tool query.
                        - If the user query is vague, use your reasoning to decide the parameters for the tool and call it. Do not ask the user for more information unless absolutely necessary.   

                        Respond in this format:

                        ACTION: toolName
                        param=value
                        """));

        messages.add(new UserMessage(query));

        return executeStep(messages, images, 0);
    }

    private Flux<String> executeStep(List<Message> messages, List<FilePart> images, int step) {

        if (step > MAX_STEPS) {
            return Flux.just("Max reasoning steps reached.");
        }

        return Mono.fromCallable(() -> chatClient.prompt()
                .messages(messages)
                .call()
                .content())

                .flatMapMany(response -> {

                    Optional<ToolCall> toolCall = toolParser.parse(response);

                    if (toolCall.isEmpty()) {

                        return Flux.just("Final Answer: " + response);
                    }

                    ToolCall call = toolCall.get();

                    return toolExecutor.execute(
                            call.getToolName(),
                            call.getArgs(),
                            images)

                            .flatMapMany(result -> {

                                messages.add(new AssistantMessage(response));

                                messages.add(new UserMessage("Tool result: " + result));

                                return Flux.concat(
                                        Flux.just("LLM requested tool: " + call.getToolName()),
                                        Flux.just("Tool result: " + result),
                                        executeStep(messages, images, step + 1));
                            });
                });
    }
}