package com.RavenDev.inventory_crafting_api.dto;

import java.util.List;

public record InventoryResponseDTO(
        Long id,
        String ownerName,
        List slots
) {
}
