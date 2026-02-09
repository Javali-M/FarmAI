package com.example.farmer.mandi_service.models;

public class MandiResponse {

    private String mandiName;
    private String commodity;
    private double price;
    private String state;
    private String district;

    public MandiResponse() {}

    public MandiResponse(String mandiName,
                         String commodity,
                         double price,
                         String state,
                         String district) {

        this.mandiName = mandiName;
        this.commodity = commodity;
        this.price = price;
        this.state = state;
        this.district = district;
    }

    public String getMandiName() {
        return mandiName;
    }

    public void setMandiName(String mandiName) {
        this.mandiName = mandiName;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }
}
