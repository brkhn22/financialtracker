package com.moneyboss.financialtracker.item.item_user;

import com.moneyboss.financialtracker.item.Item;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemUserResponse {
    private Item item;
    private Double quantity;
    private Double buyingPrice;

}

