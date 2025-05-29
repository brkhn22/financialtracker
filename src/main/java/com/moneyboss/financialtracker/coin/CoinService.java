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
