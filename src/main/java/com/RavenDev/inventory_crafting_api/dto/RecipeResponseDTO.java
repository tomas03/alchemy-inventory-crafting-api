package com.RavenDev.inventory_crafting_api.dto;

import java.util.List;

public record RecipeResponseDTO(
        Long id,
        String name,
        ItemResponseDTO resultItem,
        Integer resultQuantity,
        List< RecipeIngredientResponseDTO > ingredients
) {
}