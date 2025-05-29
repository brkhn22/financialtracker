package com.moneyboss.financialtracker.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSimpleResponse {
    
    private Integer id;
    private String email;
    private String name;
    private Boolean active;
    private Role role; 

}
