package com.example.agent_backend.model;

public class DiseaseResponse {

    private String name;
    private String description;
    private double confidence;

    public DiseaseResponse() {

    }

    public DiseaseResponse(String name, String description, double confidence) {
        this.name = name;
        this.description = description;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    @Override
    public String toString() {
        return "Disease Response {" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", confidence=" + confidence +
                '}';
    }

}
