package com.whereToTrip.whereToTrip.dto;

public class FlightOffer {
    private String id;
    private String price;
    private String currency;
    private String departureTime;
    private String arrivalTime;
    private String duration;
    private String airline;
    private String departureAirport;
    private String arrivalAirport;

    // Constructors
    public FlightOffer() {}

    public FlightOffer(String id, String price, String currency, String departureTime, 
                      String arrivalTime, String duration, String airline, String departureAirport, String arrivalAirport) {
        this.id = id;
        this.price = price;
        this.currency = currency;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.airline = airline;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }
}
