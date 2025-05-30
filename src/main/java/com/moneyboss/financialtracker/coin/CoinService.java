package com.moneyboss.financialtracker.coin;

import java.util.Collections;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinService {

    private final String BASE_URL = "https://api.coingecko.com/api/v3";
    private final RestTemplate restTemplate;

    public ResponseEntity<List<Coin>> getPopularCoins() {
        String url = BASE_URL + "/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=10&page=1&sparkline=false";
        
        try {
            log.info("Fetching popular coins from: {}", url);
            
            List<Coin> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<Coin>>() {}
            ).getBody();
            
            return ResponseEntity.ok(response != null ? response : Collections.emptyList());
            
        } catch (ResourceAccessException e) {
            log.error("Network error while fetching coins: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching coins: {} - {}", e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (HttpServerErrorException e) {
            log.error("Server error while fetching coins: {} - {}", e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (RestClientException e) {
            log.error("Unexpected error fetching coins: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            log.error("Fatal error fetching coins: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Coin>> getAllCoins(String currency){
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/coins/markets")
                .queryParam("vs_currency", currency)
                .build()
                .toUriString();
        
        try {
            log.info("Fetching all coins in {} from: {}", currency, url);
            
            List<Coin> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<Coin>>() {}
            ).getBody();
            
            return ResponseEntity.ok(response != null ? response : Collections.emptyList());
            
        } catch (ResourceAccessException e) {
            log.error("Network error while fetching all coins: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (HttpClientErrorException e) {
            log.error("Client error while fetching all coins: {} - {}", e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (HttpServerErrorException e) {
            log.error("Server error while fetching all coins: {} - {}", e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (RestClientException e) {
            log.error("Unexpected error fetching all coins: {}", e.getMessage());
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            log.error("Fatal error fetching all coins: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<Coin> getCoinById(String coinId, String currency) {
        if (coinId == null || coinId.trim().isEmpty()) {
            log.warn("Empty coin ID provided for fetching coin details");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if(currency == null || currency.trim().isEmpty()){
            log.warn("Empty currency provided for fetching coin details");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/coins/markets")
                .queryParam("vs_currency", currency.trim())
                .queryParam("ids", coinId.trim())
                .build()
                .toUriString();
        
        try {
            log.info("Fetching details for coin ID: '{}'", coinId);

            List<Coin> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Coin>>() {}
            ).getBody();

            Coin coin = (response != null && !response.isEmpty()) ? response.get(0) : null;
            return ResponseEntity.ok(coin);

        } catch (Exception e) {
            log.error("Fatal error fetching coin with ID '{}': {}", coinId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    public ResponseEntity<SearchCoinResponse> getCoinsByQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty query provided for coin search");
            return ResponseEntity.ok(SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
        }
        
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/search")
                .queryParam("query", query.trim())
                .build()
                .toUriString();
        
        try {
            log.info("Searching coins with query: '{}'", query);
            
            SearchCoinResponse response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<SearchCoinResponse>() {}
            ).getBody();
            
            return ResponseEntity.ok(response != null ? response : SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
            
        } catch (ResourceAccessException e) {
            log.error("Network error while searching coins with query '{}': {}", query, e.getMessage());
            return ResponseEntity.ok(SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
        } catch (HttpClientErrorException e) {
            log.error("Client error while searching coins with query '{}': {} - {}", 
                    query, e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
        } catch (HttpServerErrorException e) {
            log.error("Server error while searching coins with query '{}': {} - {}", 
                    query, e.getStatusCode(), e.getMessage());
            return ResponseEntity.ok(SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
        } catch (RestClientException e) {
            log.error("Unexpected error searching coins with query '{}': {}", query, e.getMessage());
            return ResponseEntity.ok(SearchCoinResponse.builder()
                    .coins(Collections.emptyList())
                    .build());
        } catch (Exception e) {
            log.error("Fatal error searching coins with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(SearchCoinResponse.builder()
                            .coins(Collections.emptyList())
                            .build());
        }
    }
}
