package com.example.agent_backend.tool;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.KnowledgeBaseRetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateConfiguration;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateInput;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateRequest;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateResponse;
import software.amazon.awssdk.services.bedrockagentruntime.model.RetrieveAndGenerateType;

@Service
@RequiredArgsConstructor
public class CropDiseaseKnowledgeTool {

    private final BedrockAgentRuntimeClient client;

    private static final String KNOWLEDGE_BASE_ID = "ATAETDVRUO";

    private static final String MODEL_ARN = "arn:aws:bedrock:us-east-1:195215829556:inference-profile/us.amazon.nova-pro-v1:0";

    public Mono<String> searchDiseaseKnowledge(String disease) {

        if(disease == null || disease.isEmpty()) {
            return Mono.just("Disease name is required, which is not present in the input!!");
        }

        String query = """
                Provide cause, symptoms, treatment, and prevention for the crop disease: %s
                """.formatted(disease);

        System.out.println("searchDiseaseKnowledge query=" + query);

        return Mono.fromCallable(() -> {

            RetrieveAndGenerateRequest request = RetrieveAndGenerateRequest.builder()
                    .input(RetrieveAndGenerateInput.builder()
                            .text(query)
                            .build())
                    .retrieveAndGenerateConfiguration(
                            RetrieveAndGenerateConfiguration.builder()
                                    .type(RetrieveAndGenerateType.KNOWLEDGE_BASE)
                                    .knowledgeBaseConfiguration(
                                            KnowledgeBaseRetrieveAndGenerateConfiguration.builder()
                                                    .knowledgeBaseId(KNOWLEDGE_BASE_ID)
                                                    .modelArn(MODEL_ARN)
                                                    .build())
                                    .build())
                    .build();

            RetrieveAndGenerateResponse response = client.retrieveAndGenerate(request);

            return response.output().text();
        });
    }
}
