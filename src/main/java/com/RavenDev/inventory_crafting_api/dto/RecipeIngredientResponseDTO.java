package com.RavenDev.inventory_crafting_api.dto;

public record RecipeIngredientResponseDTO(
        Long id,
        ItemResponseDTO item,
        Integer quantity
) {
}