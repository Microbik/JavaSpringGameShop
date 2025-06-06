package com.example.gameplatform.repository;

import com.example.gameplatform.model.UserAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddonRepository extends JpaRepository<UserAddon, Long> {
    boolean existsByUserIdAndAddonId(Long userId, Long addonId);
}