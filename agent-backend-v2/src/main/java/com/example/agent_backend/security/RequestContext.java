package com.example.agent_backend.security;

public class RequestContext {

    private static final ThreadLocal<String> emailHolder = new ThreadLocal<>();
    private static final ThreadLocal<Double> latitudeHolder = new ThreadLocal<>();
    private static final ThreadLocal<Double> longitudeHolder = new ThreadLocal<>();

    public static void setEmail(String email) { emailHolder.set(email); }
    public static void setLatitude(Double lat) { latitudeHolder.set(lat); }
    public static void setLongitude(Double lng) { longitudeHolder.set(lng); }

    public static String getEmail() { return emailHolder.get(); }
    public static Double getLatitude() { return latitudeHolder.get(); }
    public static Double getLongitude() { return longitudeHolder.get(); }

    public static void clear() {
        emailHolder.remove();
        latitudeHolder.remove();
        longitudeHolder.remove();
    }
}
