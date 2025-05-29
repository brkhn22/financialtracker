package com.moneyboss.financialtracker.coin;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCoin {
    private String id;
    private String name;
    private String symbol;

    @JsonProperty("api_symbol")
    private String apiSymbol;
    
    private String thumb;
    private String large;
}
