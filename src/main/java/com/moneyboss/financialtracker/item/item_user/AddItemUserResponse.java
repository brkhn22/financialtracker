package com.moneyboss.financialtracker.item.item_user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddItemUserResponse {
    private String itemId;
    private Double quantity;
    private Double buyingPrice;

}

