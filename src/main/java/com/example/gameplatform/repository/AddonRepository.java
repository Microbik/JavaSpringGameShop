package com.example.gameplatform.repository;

import com.example.gameplatform.model.Addon;
import com.example.gameplatform.model.UserAddon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonRepository extends JpaRepository<Addon, Long> {}