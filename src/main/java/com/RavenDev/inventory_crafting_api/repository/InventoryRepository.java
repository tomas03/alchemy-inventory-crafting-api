package com.RavenDev.inventory_crafting_api.repository;

import com.RavenDev.inventory_crafting_api.domain.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long>{
}