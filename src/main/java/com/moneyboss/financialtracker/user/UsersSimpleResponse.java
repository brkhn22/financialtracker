package com.moneyboss.financialtracker.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsersSimpleResponse {
    private List<UserSimpleResponse> users;
}
