package com.RavenDev.inventory_crafting_api.controller;

import com.RavenDev.inventory_crafting_api.dto.CreateRecipeRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.InventoryResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.RecipeResponseDTO;
import com.RavenDev.inventory_crafting_api.service.CraftingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crafting")
public class CraftingController {

    private final CraftingService craftingService;

    public CraftingController(CraftingService craftingService) {
        this.craftingService = craftingService;
    }

    @PostMapping("/recipes")
    public ResponseEntity< RecipeResponseDTO > createRecipe(@Valid @RequestBody CreateRecipeRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(craftingService.createRecipe(dto));
    }

    @PostMapping("/inventories/{inventoryId}/craft/{recipeId}")
    public ResponseEntity< InventoryResponseDTO > craft(
            @PathVariable Long inventoryId,
            @PathVariable Long recipeId
    ) {
        return ResponseEntity.ok(craftingService.craft(inventoryId, recipeId));
    }
}