package com.whereToTrip.whereToTrip.service;

import com.amadeus.Amadeus;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.whereToTrip.whereToTrip.dto.FlightSearchRequest;
import com.whereToTrip.whereToTrip.dto.FlightSearchResponse;
import com.whereToTrip.whereToTrip.dto.FlightOffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FlightSearchService {

    @Autowired
    private Amadeus amadeus;

    // 예산별 추천 목적지 목록
    private static final List<String> BUDGET_DESTINATIONS = Arrays.asList(
        "NRT", "ICN", "BKK", "SIN", "KUL", "MNL", "CGK", "HKG", "TPE", "MNL"
    );

    // 예산별 목적지 정보 (예상 가격 기준)
    private static final List<DestinationInfo> DESTINATION_INFOS = Arrays.asList(
        new DestinationInfo("NRT", "도쿄", "일본", 200000),
        new DestinationInfo("ICN", "서울", "한국", 100000),
        new DestinationInfo("BKK", "방콕", "태국", 250000),
        new DestinationInfo("SIN", "싱가포르", "싱가포르", 350000),
        new DestinationInfo("KUL", "쿠알라룸푸르", "말레이시아", 200000),
        new DestinationInfo("MNL", "마닐라", "필리핀", 180000),
        new DestinationInfo("CGK", "자카르타", "인도네시아", 220000),
        new DestinationInfo("HKG", "홍콩", "홍콩", 300000),
        new DestinationInfo("TPE", "타이베이", "대만", 250000),
        new DestinationInfo("HAN", "하노이", "베트남", 200000),
        new DestinationInfo("SGN", "호치민", "베트남", 220000),
        new DestinationInfo("CNX", "치앙마이", "태국", 230000),
        new DestinationInfo("DPS", "발리", "인도네시아", 280000),
        new DestinationInfo("CEB", "세부", "필리핀", 200000),
        new DestinationInfo("PEN", "페낭", "말레이시아", 180000)
    );

    public FlightSearchResponse searchFlights(FlightSearchRequest request) {
        try {
            System.out.println("=== 항공권 검색 시작 ===");
            System.out.println("출발지: " + request.getOrigin());
            System.out.println("도착지: " + request.getDestination());
            System.out.println("출발일: " + request.getDepartureDate());
            System.out.println("성인 수: " + request.getAdults());
            System.out.println("최대 가격: " + request.getMaxPrice());
            
            // 출발지에서 도착지로의 항공권 검색
            FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                com.amadeus.Params.with("originLocationCode", request.getOrigin())
                    .and("destinationLocationCode", request.getDestination())
                    .and("departureDate", request.getDepartureDate())
                    .and("adults", request.getAdults())
            );

            System.out.println("API 응답 받음. 항공권 수: " + (flightOffers != null ? flightOffers.length : 0));
            
            // 전체 응답 구조 확인
            if (flightOffers != null && flightOffers.length > 0) {
                System.out.println("=== 첫 번째 항공권 전체 정보 ===");
                FlightOfferSearch firstOffer = flightOffers[0];
                System.out.println("ID: " + firstOffer.getId());
                System.out.println("Price: " + firstOffer.getPrice());
                System.out.println("Itineraries: " + (firstOffer.getItineraries() != null ? firstOffer.getItineraries().length : 0));
                if (firstOffer.getItineraries() != null && firstOffer.getItineraries().length > 0) {
                    System.out.println("첫 번째 여정 세그먼트 수: " + (firstOffer.getItineraries()[0].getSegments() != null ? firstOffer.getItineraries()[0].getSegments().length : 0));
                }
                System.out.println("=== 전체 응답 끝 ===");
            }

            List<FlightOffer> offers = new ArrayList<>();
            
            if (flightOffers != null) {
                for (int i = 0; i < flightOffers.length; i++) {
                    FlightOfferSearch offer = flightOffers[i];
                    System.out.println("=== 항공권 " + (i+1) + " ===");
                    System.out.println("가격: " + (offer.getPrice() != null ? offer.getPrice().getTotal() : "null"));
                    System.out.println("통화: " + (offer.getPrice() != null ? offer.getPrice().getCurrency() : "null"));
                    
                    // 예산 필터링
                    if (offer.getPrice() != null && 
                        offer.getPrice().getTotal() <= request.getMaxPrice()) {
                        
                        offers.add(convertToFlightOffer(offer));
                        System.out.println("✅ 필터링 통과: " + offer.getPrice().getTotal());
                    } else {
                        System.out.println("❌ 예산 초과로 제외: " + (offer.getPrice() != null ? offer.getPrice().getTotal() : "null"));
                    }
                }
            }

            System.out.println("최종 결과: " + offers.size() + "개 항공권");
            return new FlightSearchResponse(offers, offers.size());

        } catch (ResponseException e) {
            System.err.println("Amadeus API 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("항공권 검색 중 오류가 발생했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("일반 오류: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("항공권 검색 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public List<DestinationInfo> getRecommendedDestinations(int maxBudget) {
        return DESTINATION_INFOS.stream()
                .filter(dest -> dest.getEstimatedPrice() <= maxBudget)
                .toList();
    }

    public FlightSearchResponse searchFlightsByCountry(String origin, String destination, String departureDate, int adults, int maxPrice) {
        FlightSearchRequest request = new FlightSearchRequest();
        request.setOrigin(origin);
        request.setDestination(destination);
        request.setDepartureDate(departureDate);
        request.setAdults(adults);
        request.setMaxPrice(maxPrice);
        
        return searchFlights(request);
    }

    public List<String> searchAirports(String keyword) {
        try {
            Location[] locations = amadeus.referenceData.locations.get(
                com.amadeus.Params.with("keyword", keyword)
                    .and("subType", "AIRPORT")
            );

            List<String> airports = new ArrayList<>();
            if (locations != null) {
                for (Location location : locations) {
                    airports.add(location.getIataCode() + " - " + location.getName());
                }
            }
            return airports;

        } catch (ResponseException e) {
            throw new RuntimeException("공항 검색 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    private FlightOffer convertToFlightOffer(FlightOfferSearch offer) {
        System.out.println("=== convertToFlightOffer 시작 ===");
        FlightOffer flightOffer = new FlightOffer();
        flightOffer.setId(offer.getId());
        flightOffer.setPrice(String.valueOf(offer.getPrice().getTotal()));
        flightOffer.setCurrency(offer.getPrice().getCurrency());
        
        System.out.println("기본 정보 설정 완료 - ID: " + offer.getId() + ", 가격: " + offer.getPrice().getTotal());
        
        if (offer.getItineraries() != null && offer.getItineraries().length > 0) {
            System.out.println("여정 정보 있음: " + offer.getItineraries().length + "개");
            var itinerary = offer.getItineraries()[0];
            System.out.println("첫 번째 여정 duration: " + itinerary.getDuration());
            
            if (itinerary.getSegments() != null && itinerary.getSegments().length > 0) {
                System.out.println("세그먼트 정보 있음: " + itinerary.getSegments().length + "개");
                var segment = itinerary.getSegments()[0];
                System.out.println("첫 번째 세그먼트 - 출발: " + segment.getDeparture().getAt() + 
                                 " (" + segment.getDeparture().getIataCode() + ")" +
                                 ", 도착: " + segment.getArrival().getAt() + 
                                 " (" + segment.getArrival().getIataCode() + ")" +
                                 ", 항공사: " + segment.getCarrierCode());
                
                flightOffer.setDepartureTime(segment.getDeparture().getAt());
                flightOffer.setArrivalTime(segment.getArrival().getAt());
                flightOffer.setDuration(itinerary.getDuration());
                flightOffer.setAirline(segment.getCarrierCode());
                flightOffer.setDepartureAirport(segment.getDeparture().getIataCode());
                flightOffer.setArrivalAirport(segment.getArrival().getIataCode());
            } else {
                System.out.println("세그먼트 정보 없음");
            }
        } else {
            System.out.println("여정 정보 없음");
        }
        
        System.out.println("=== convertToFlightOffer 완료 ===");
        return flightOffer;
    }

    // 목적지 정보를 위한 내부 클래스
    public static class DestinationInfo {
        private String code;
        private String city;
        private String country;
        private int estimatedPrice;

        public DestinationInfo(String code, String city, String country, int estimatedPrice) {
            this.code = code;
            this.city = city;
            this.country = country;
            this.estimatedPrice = estimatedPrice;
        }

        // Getters and Setters
        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
        public int getEstimatedPrice() { return estimatedPrice; }
        public void setEstimatedPrice(int estimatedPrice) { this.estimatedPrice = estimatedPrice; }
    }
}
