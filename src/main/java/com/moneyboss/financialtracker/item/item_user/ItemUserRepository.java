package com.moneyboss.financialtracker.item.item_user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemUserRepository extends JpaRepository<ItemUser, Integer> {

    Optional<List<ItemUser>> findByItemNameAndUserId(String itemName, Integer userId);
    Optional<List<ItemUser>> findByUserId(Integer userId);
}
