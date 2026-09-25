package com.RavenDev.inventory_crafting_api.repository;

import com.RavenDev.inventory_crafting_api.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
