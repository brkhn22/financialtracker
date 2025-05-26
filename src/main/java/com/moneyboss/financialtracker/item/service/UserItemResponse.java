package com.moneyboss.financialtracker.item.service;

import java.util.List;

import com.moneyboss.financialtracker.item.item_user.ItemUser;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserItemResponse {

    private List<ItemUser> items;
}
