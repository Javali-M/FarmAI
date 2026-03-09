package com.example.agent_backend.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.example.agent_backend.agent.AgentService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

import org.springframework.web.bind.annotation.PostMapping;

import com.example.agent_backend.security.RequestContext;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agent")
public class AgentController {

    private final AgentService agentService;
    private final RequestContext requestContext;

    @PostMapping(value = "/ask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> ask(@RequestPart("message") String query,
            @RequestPart(value = "images", required = false) List<FilePart> images) {

        if (requestContext.getEmail() == null) {
            return Flux.error(new RuntimeException("Unauthorized user!!"));
        }

        // System.out.println("email: " + requestContext.getEmail());
        // System.out.println("latitude: " + requestContext.getLatitude());
        // System.out.println("longitude: " + requestContext.getLongitude());

        return agentService.runAgent(query, images);

    }

}
