package com.example.farmer.plant_disease_microservice.models;

import java.util.List;

public class PlantNetApiResponse {

    public List<Result> results;

    public static class Result {

        public String name;
        public String description;
        public double score;
    }
}
