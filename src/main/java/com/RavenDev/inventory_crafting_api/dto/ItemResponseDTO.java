package com.RavenDev.inventory_crafting_api.dto;

public record ItemResponseDTO(Long id, String name, com.RavenDev.inventory_crafting_api.domain.ItemType type, Double value) {
}
