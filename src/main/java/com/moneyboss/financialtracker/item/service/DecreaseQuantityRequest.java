package com.moneyboss.financialtracker.item.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DecreaseQuantityRequest {
    private String itemId;
    private Double quantity;
    private String currency;
}
