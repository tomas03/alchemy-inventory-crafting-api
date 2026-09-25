package com.RavenDev.inventory_crafting_api.dto;

import com.RavenDev.inventory_crafting_api.domain.ItemType;

public record ItemRequestDTO(String name, ItemType type, Double value) {
}
