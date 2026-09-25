package com.RavenDev.inventory_crafting_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateRecipeRequestDTO(
        @NotBlank(message = "El nombre de la receta es obligatorio")
        String name,

        @NotNull(message = "El id del item resultante es obligatorio")
        Long resultItemId,

        @NotNull(message = "La cantidad resultante es obligatoria")
        Integer resultQuantity,

        @NotEmpty(message = "La receta debe tener al menos un ingrediente")
        @Valid
        List< RecipeIngredientRequestDTO > ingredients
) {
}