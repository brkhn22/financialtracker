package com.moneyboss.financialtracker.item.item_user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUserTotal {
    private List<ItemCoin> itemCoins;
    private Double totalProfit;
}
