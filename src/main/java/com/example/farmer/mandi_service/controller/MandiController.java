package com.example.farmer.mandi_service.controller;

import com.example.farmer.mandi_service.models.MandiResponse;
import com.example.farmer.mandi_service.service.MandiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/mandi")
public class MandiController {

    private final MandiService mandiService;

    public MandiController(MandiService mandiService) {
        this.mandiService = mandiService;
    }

    @GetMapping("/prices")
    public Mono<List<MandiResponse>> getPrices(
            @RequestParam String state,
            @RequestParam String commodity) {

        return mandiService.getMandiPrices(state, commodity);
    }
}
