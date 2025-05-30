package com.moneyboss.financialtracker.item.service;

import com.moneyboss.financialtracker.coin.Coin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DecreaseQuantityResponse {
    private Double totalPrice;
    private Double quantityRemoved;
    private Coin coin;
}
