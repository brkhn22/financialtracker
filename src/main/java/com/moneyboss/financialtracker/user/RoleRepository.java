package com.moneyboss.financialtracker.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByName(String name);
}
