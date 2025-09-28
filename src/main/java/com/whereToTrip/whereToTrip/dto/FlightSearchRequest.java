package com.whereToTrip.whereToTrip.dto;

public class FlightSearchRequest {
    private String origin;
    private String destination;
    private String departureDate;
    private int adults = 1;
    private double maxPrice = 300000; // 기본 최대 가격 30만원

    // Constructors
    public FlightSearchRequest() {}

    public FlightSearchRequest(String origin, String destination, String departureDate, int adults, double maxPrice) {
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.adults = adults;
        this.maxPrice = maxPrice;
    }

    // Getters and Setters
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}
