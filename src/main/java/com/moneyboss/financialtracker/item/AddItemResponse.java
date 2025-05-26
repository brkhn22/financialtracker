package com.moneyboss.financialtracker.item;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddItemResponse {

    private Item item;
}
