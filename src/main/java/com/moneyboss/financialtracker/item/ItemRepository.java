package com.moneyboss.financialtracker.item;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findByName(String name);
}
