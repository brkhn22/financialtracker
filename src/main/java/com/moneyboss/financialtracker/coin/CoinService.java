package com.moneyboss.financialtracker.coin;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinService {

    private static final String BASE_URL = "https://api.coingecko.com/api/v3";
    private final RestTemplate restTemplate;
    private final CoinRepository coinRepository;
    private final CoinCurrencyRepository coinCurrencyRepository;

    public ResponseEntity<List<Coin>> getPopularCoins(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        try {
            log.info("Fetching popular coins from database for currency: {}", currency);
            
            List<CoinEntity> coinEntities = coinRepository.findByCurrency(currency.toUpperCase());
            
            if (coinEntities.isEmpty()) {
                log.warn("No coins found in database for currency: {}", currency);
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            // Get top 10 coins by market cap
            List<Coin> popularCoins = coinEntities.stream()
                    .map(entity -> convertToDto(entity, currency.toUpperCase()))
                    .filter(coin -> coin.getMarketCap() != null)
                    .sorted((c1, c2) -> Double.compare(c2.getMarketCap(), c1.getMarketCap()))
                    .limit(10)
                    .toList();
            
            log.info("Successfully fetched {} popular coins from database", popularCoins.size());
            return ResponseEntity.ok(popularCoins);
            
        } catch (Exception e) {
            log.error("Fatal error fetching popular coins from database: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Coin>> getAllCoins(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        try {
            log.info("Fetching all coins from database for currency: {}", currency);
            
            List<CoinEntity> coinEntities = coinRepository.findByCurrency(currency.toUpperCase());
            
            if (coinEntities.isEmpty()) {
                log.warn("No coins found in database for currency: {}", currency);
                return ResponseEntity.ok(Collections.emptyList());
            }
            
            List<Coin> coins = coinEntities.stream()
                    .map(entity -> convertToDto(entity, currency.toUpperCase()))
                    .toList();
            
            log.info("Successfully fetched {} coins from database", coins.size());
            return ResponseEntity.ok(coins);
            
        } catch (Exception e) {
            log.error("Fatal error fetching all coins from database: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<Coin> getCoinById(String coinId, String currency) {
        if (coinId == null || coinId.trim().isEmpty()) {
            log.warn("Empty coin ID provided for fetching coin details");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        if (currency == null || currency.trim().isEmpty()) {
            log.warn("Empty currency provided for fetching coin details");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            log.info("Fetching coin from database with ID: '{}' and currency: '{}'", coinId, currency);

            Optional<CoinEntity> coinEntity = coinRepository.findByIdAndCurrency(coinId.trim(), currency.toUpperCase());

            if (coinEntity.isEmpty()) {
                log.warn("Coin not found in database with ID: '{}' and currency: '{}'", coinId, currency);
                return ResponseEntity.ok(null);
            }

            Coin coin = convertToDto(coinEntity.get(), currency.toUpperCase());
            log.info("Successfully fetched coin from database: {}", coin.getName());
            return ResponseEntity.ok(coin);

        } catch (Exception e) {
            log.error("Fatal error fetching coin with ID '{}': {}", coinId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<List<Coin>> getCoinsByQuery(String query, String currency) {
        if (query == null || query.trim().isEmpty()) {
            log.warn("Empty query provided for coin search");
            return ResponseEntity.ok(Collections.emptyList());
        }

        try {
            log.info("Searching coins in database with query: '{}' and currency: '{}'", query, currency);
            
            List<CoinEntity> coinEntities = coinRepository.findByCurrency(currency.toUpperCase());
            
            // Filter coins by query (name or symbol contains the query)
            List<Coin> searchCoins = coinEntities.stream()
                    .filter(entity -> 
                        entity.getName().toLowerCase().contains(query.toLowerCase()) ||
                        entity.getSymbol().toLowerCase().contains(query.toLowerCase()) ||
                        entity.getId().toLowerCase().contains(query.toLowerCase())
                    )
                    .limit(20) // Limit search results
                    .map(entity -> convertToDto(entity, currency.toUpperCase()))
                    .toList();
            
            log.info("Successfully found {} coins in database for query: '{}'", searchCoins.size(), query);
            return ResponseEntity.ok(searchCoins);
            
        } catch (Exception e) {
            log.error("Fatal error searching coins with query '{}': {}", query, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Coin>> fetchAllCoinsByCurrency(String currency, Integer page, Integer per_page) {
        if (currency == null || currency.trim().isEmpty()) {
            log.warn("Empty currency provided for fetching all coins");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        }

        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/coins/markets")
                .queryParam("vs_currency", currency)
                .queryParam("page", page)
                .queryParam("per_page", per_page)
                .queryParam("order", "market_cap_desc")
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

            if (response == null) {
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Save coins to database
            for (Coin coin : response) {
                saveCoinToDatabase(coin, currency.toUpperCase());
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Fatal error fetching all coins in {}: {}", currency, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Coin>> updateAllCoins(String currency, Integer page, Integer perPage) {
        String url = UriComponentsBuilder
                .fromUriString(BASE_URL + "/coins/markets")
                .queryParam("vs_currency", currency.trim())
                .queryParam("page", page)
                .queryParam("per_page", perPage)
                .queryParam("order", "market_cap_desc")
                .build()
                .toUriString();
        
        try {
            List<Coin> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Coin>>() {}
            ).getBody();
            
            if (response == null || response.isEmpty()) {
                log.warn("No coins returned from API for currency: {}, page: {}", currency, page);
                return ResponseEntity.ok(Collections.emptyList());
            }

            // Extract coin IDs from API response
            List<String> coinIdsFromApi = response.stream()
                    .map(Coin::getId)
                    .toList();
            
            // Get existing coins for this currency that are in the API response
            List<CoinEntity> existingCoins = coinRepository.findByCurrency(currency.toUpperCase())
                    .stream()
                    .filter(entity -> coinIdsFromApi.contains(entity.getId()))
                    .toList();
            
            Set<String> existingCoinIds = existingCoins.stream()
                    .map(CoinEntity::getId)
                    .collect(Collectors.toSet());
            
            int updatedCount = 0;
            int newCount = 0;
            
            // Process each coin from API
            for (Coin coinFromApi : response) {
                if (existingCoinIds.contains(coinFromApi.getId())) {
                    // Update existing coin
                    saveCoinToDatabase(coinFromApi, currency.toUpperCase());
                    updatedCount++;
                } else {
                    // Add new coin
                    saveCoinToDatabase(coinFromApi, currency.toUpperCase());
                    newCount++;
                }
            }
            
            // Get the final updated coins from database (only the ones we just processed)
            List<CoinEntity> processedEntities = coinRepository.findAllById(coinIdsFromApi);
            List<Coin> resultCoins = processedEntities.stream()
                    .map(entity -> convertToDto(entity, currency.toUpperCase()))
                    .filter(coin -> coin.getCurrentPrice() != null) // Only include coins with valid currency data
                    .toList();
            
            log.info("Successfully processed {} coins for currency: {}, page: {} (Updated: {}, New: {})", 
                    resultCoins.size(), currency, page, updatedCount, newCount);
            
            return ResponseEntity.ok(resultCoins);
            
        } catch (Exception e) {
            log.error("Fatal error updating coins for currency: {}, page: {}: {}", currency, page, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    private void saveCoinToDatabase(Coin coin, String currency) {
        try {
            // Check if coin already exists
            Optional<CoinEntity> existingCoin = coinRepository.findById(coin.getId());
            
            CoinEntity coinEntity;
            if (existingCoin.isPresent()) {
                // Update existing coin metadata
                coinEntity = existingCoin.get();
                coinEntity.setSymbol(coin.getSymbol());
                coinEntity.setName(coin.getName());
                coinEntity.setImage(coin.getImage());
                coinEntity.setLastUpdated(LocalDateTime.now());
                coinRepository.save(coinEntity);
            } else {
                // Create new coin
                coinEntity = CoinEntity.builder()
                    .id(coin.getId())
                    .symbol(coin.getSymbol())
                    .name(coin.getName())
                    .image(coin.getImage())
                    .lastUpdated(LocalDateTime.now())
                    .build();
                coinEntity = coinRepository.save(coinEntity);
            }
            
            // Check if currency data exists for this coin
            Optional<CoinCurrencyEntity> existingCurrency = coinCurrencyRepository.findByCoinIdAndCurrency(coin.getId(), currency);
            
            if (existingCurrency.isPresent()) {
                // Update existing currency data
                CoinCurrencyEntity currencyEntity = existingCurrency.get();
                currencyEntity.setCurrentPrice(coin.getCurrentPrice());
                currencyEntity.setMarketCap(coin.getMarketCap());
                currencyEntity.setTotalVolume(coin.getTotalVolume());
                currencyEntity.setPriceChange24h(coin.getPriceChange24h());
                currencyEntity.setPriceChangePercentage24h(coin.getPriceChangePercentage24h());
                currencyEntity.setLastUpdated(LocalDateTime.now());
                coinCurrencyRepository.save(currencyEntity);
            } else {
                // Create new currency data
                CoinCurrencyEntity currencyEntity = CoinCurrencyEntity.builder()
                    .currentPrice(coin.getCurrentPrice())
                    .marketCap(coin.getMarketCap())
                    .totalVolume(coin.getTotalVolume())
                    .priceChange24h(coin.getPriceChange24h())
                    .priceChangePercentage24h(coin.getPriceChangePercentage24h())
                    .lastUpdated(LocalDateTime.now())
                    .currency(currency)
                    .coin(coinEntity)
                    .build();
                coinCurrencyRepository.save(currencyEntity);
            }
            
        } catch (Exception e) {
            log.error("Error saving coin {} for currency {}: {}", coin.getId(), currency, e.getMessage());
        }
    }

    // Helper method to convert CoinEntity to Coin DTO for specific currency
    private Coin convertToDto(CoinEntity entity, String currency) {
        // Instead of using entity.getCurrencies(), query the currency directly
        Optional<CoinCurrencyEntity> currencyEntity = coinCurrencyRepository
                .findByCoinIdAndCurrency(entity.getId(), currency);
        
        if (currencyEntity.isEmpty()) {
            // Return coin with null price data if currency not found
            return new Coin(
                entity.getId(),
                entity.getSymbol(),
                entity.getName(),
                entity.getImage(),
                null, null, null, null, null,
                entity.getLastUpdated()
            );
        }
        
        CoinCurrencyEntity curr = currencyEntity.get();
        return new Coin(
            entity.getId(),
            entity.getSymbol(),
            entity.getName(),
            entity.getImage(),
            curr.getCurrentPrice(),
            curr.getMarketCap(),
            curr.getTotalVolume(),
            curr.getPriceChange24h(),
            curr.getPriceChangePercentage24h(),
            curr.getLastUpdated()
        );
    }
}