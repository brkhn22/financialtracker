package com.moneyboss.financialtracker.coin;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coin {
    private String id;
    private String symbol;
    private String name;
    private String image;

    @JsonProperty("current_price")
    private Double currentPrice;
    @JsonProperty("market_cap")
    private Double marketCap;
    @JsonProperty("total_volume")
    private Double totalVolume;
    @JsonProperty("price_change_24h")
    private Double priceChange24h;
    @JsonProperty("price_change_percentage_24h")
    private Double priceChangePercentage24h;
    @JsonProperty("last_updated")
    private LocalDateTime lastUpdated;
}
