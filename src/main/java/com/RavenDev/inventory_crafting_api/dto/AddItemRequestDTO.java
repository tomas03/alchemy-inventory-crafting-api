package com.RavenDev.inventory_crafting_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddItemRequestDTO(
        @NotNull(message = "El id del item es obligatorio")
        Long itemId,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value=1,message = "La cantidad debe ser al menos 1")
        Integer quantity
) {
}
