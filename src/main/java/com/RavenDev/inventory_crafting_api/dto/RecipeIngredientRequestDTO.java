package com.RavenDev.inventory_crafting_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record RecipeIngredientRequestDTO(
        @NotNull(message = "El id del item ingrediente es obligatorio")
        Long itemId,

        @NotNull(message = "La cantidad requerida es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        Integer quantity
) {
}