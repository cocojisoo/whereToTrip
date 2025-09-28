package com.whereToTrip.whereToTrip.dto;

import java.util.List;

public class FlightSearchResponse {
    private List<FlightOffer> offers;
    private int totalCount;

    // Constructors
    public FlightSearchResponse() {}

    public FlightSearchResponse(List<FlightOffer> offers, int totalCount) {
        this.offers = offers;
        this.totalCount = totalCount;
    }

    // Getters and Setters
    public List<FlightOffer> getOffers() {
        return offers;
    }

    public void setOffers(List<FlightOffer> offers) {
        this.offers = offers;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
