package com.moneyboss.financialtracker.coin;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CoinRepository extends JpaRepository<CoinEntity, String> {
    
    @Query("SELECT DISTINCT c FROM CoinEntity c JOIN c.currencies curr WHERE curr.currency = :currency")
    List<CoinEntity> findByCurrency(@Param("currency") String currency);
    
    @Query("SELECT c FROM CoinEntity c JOIN c.currencies curr WHERE c.id = :coinId AND curr.currency = :currency")
    Optional<CoinEntity> findByIdAndCurrency(@Param("coinId") String coinId, @Param("currency") String currency);
}