package com.moneyboss.financialtracker.item.item_user;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCoin {
    @JsonProperty("id")
    private String coinId;
    @JsonProperty("name")
    private String coinName;
    @JsonProperty("symbol")
    private String coinSymbol;
    @JsonProperty("image")
    private String coinImage;
    @JsonProperty("current_price")
    private Double coinCurrentPrice;
    @JsonProperty("price_change_percentage_24h")
    private Double coinPriceChangePercentage24h;
    @JsonProperty("price_change_24h")
    private Double coinPriceChange24h;
    @JsonProperty("last_updated")
    private LocalDateTime coinLastUpdated;

    @JsonProperty("item_id")
    private Integer itemId;
    private Double coinBuyingPrice;
    private Double coinQuantity;
    private LocalDateTime coinInsertedAt;
}
