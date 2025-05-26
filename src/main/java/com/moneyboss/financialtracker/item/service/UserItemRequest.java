package com.moneyboss.financialtracker.item.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserItemRequest {

    private String token;
    private Integer userId;
}
