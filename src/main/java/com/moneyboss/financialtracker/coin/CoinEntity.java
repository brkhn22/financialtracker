package com.moneyboss.financialtracker.coin;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoinEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "coin")
    private List<CoinCurrencyEntity> currencies;
}