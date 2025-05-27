package com.moneyboss.financialtracker.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateItemRequest {
    
    private String originalName;
    private String newSymbolPath;
    private String newName;
}
