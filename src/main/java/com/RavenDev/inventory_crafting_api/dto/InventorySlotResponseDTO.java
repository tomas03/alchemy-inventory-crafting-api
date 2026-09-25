package com.RavenDev.inventory_crafting_api.dto;

public record InventorySlotResponseDTO(
        Long slotId,
        ItemResponseDTO item,
        Integer quantity
) {
}
