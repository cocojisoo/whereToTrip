package com.whereToTrip.whereToTrip.controller;

import com.whereToTrip.whereToTrip.dto.FlightSearchRequest;
import com.whereToTrip.whereToTrip.dto.FlightSearchResponse;
import com.whereToTrip.whereToTrip.service.FlightSearchService;
import com.whereToTrip.whereToTrip.service.FlightSearchService.DestinationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
@CrossOrigin(origins = "*")
public class FlightController {

    @Autowired
    private FlightSearchService flightSearchService;

    @PostMapping("/search")
    public ResponseEntity<FlightSearchResponse> searchFlights(@RequestBody FlightSearchRequest request) {
        try {
            FlightSearchResponse response = flightSearchService.searchFlights(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/destinations")
    public ResponseEntity<List<DestinationInfo>> getRecommendedDestinations(
            @RequestParam(defaultValue = "300000") int maxBudget) {
        try {
            List<DestinationInfo> destinations = flightSearchService.getRecommendedDestinations(maxBudget);
            return ResponseEntity.ok(destinations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/airports")
    public ResponseEntity<List<String>> searchAirports(@RequestParam String keyword) {
        try {
            List<String> airports = flightSearchService.searchAirports(keyword);
            return ResponseEntity.ok(airports);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search/country")
    public ResponseEntity<FlightSearchResponse> searchFlightsByCountry(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String departureDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "300000") int maxPrice) {
        try {
            FlightSearchResponse response = flightSearchService.searchFlightsByCountry(
                origin, destination, departureDate, adults, maxPrice);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("컨트롤러 오류: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> testAmadeus() {
        try {
            // 간단한 공항 검색으로 Amadeus API 연결 테스트
            List<String> airports = flightSearchService.searchAirports("Tokyo");
            return ResponseEntity.ok("Amadeus API 연결 성공! 검색된 공항: " + airports.toString());
        } catch (Exception e) {
            System.err.println("Amadeus API 테스트 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Amadeus API 연결 실패: " + e.getMessage());
        }
    }

    @GetMapping("/test-flight")
    public ResponseEntity<FlightSearchResponse> testFlightSearch() {
        try {
            // 실제 항공권 검색 테스트 (ICN -> NRT)
            FlightSearchRequest request = new FlightSearchRequest();
            request.setOrigin("ICN");
            request.setDestination("NRT");
            request.setDepartureDate("2025-10-01");
            request.setAdults(1);
            request.setMaxPrice(500000);
            
            FlightSearchResponse response = flightSearchService.searchFlights(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("항공권 검색 테스트 실패: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}
