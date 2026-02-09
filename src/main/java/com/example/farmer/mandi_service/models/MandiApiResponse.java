package com.example.farmer.mandi_service.models;

import java.util.List;

public class MandiApiResponse {

    public List<Record> records;

    public static class Record {

        public String State;
        public String District;
        public String Market;
        public String Commodity;
        public String Modal_Price;
        public String Arrival_Date;
    }
}