package com.moneyboss.financialtracker.item.item_user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemUserRepository extends JpaRepository<ItemUser, Integer> {

    Optional<List<ItemUser>> findByItemIdAndUserId(String itemId, Integer userId);
    Optional<ItemUser> findById(Integer id);
    Optional<List<ItemUser>> findByUserId(Integer userId);
}
