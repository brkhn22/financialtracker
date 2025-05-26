package com.moneyboss.financialtracker.item.item_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddItemUserRequest {
    private String token;
    private Integer userId;
    private String itemName;
    private Double quantity;
    private Double buyingPrice;

}
