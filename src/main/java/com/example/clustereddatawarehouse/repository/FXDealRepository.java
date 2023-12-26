package com.example.clustereddatawarehouse.repository;

import com.example.clustereddatawarehouse.entity.FXDeal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FXDealRepository extends JpaRepository<FXDeal, String> {
    boolean existsByDealUniqueId(String dealUniqueId);
}

