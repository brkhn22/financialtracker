package com.moneyboss.financialtracker.coin;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinCurrencyRepository extends JpaRepository<CoinCurrencyEntity, Long> {
    
    Optional<CoinCurrencyEntity> findByCoinIdAndCurrency(String coinId, String currency);
}