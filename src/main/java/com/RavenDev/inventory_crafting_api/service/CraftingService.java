package com.RavenDev.inventory_crafting_api.service;

import com.RavenDev.inventory_crafting_api.domain.Inventory;
import com.RavenDev.inventory_crafting_api.domain.InventorySlot;
import com.RavenDev.inventory_crafting_api.domain.Item;
import com.RavenDev.inventory_crafting_api.domain.Recipe;
import com.RavenDev.inventory_crafting_api.domain.RecipeIngredient;
import com.RavenDev.inventory_crafting_api.dto.CreateRecipeRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.InventoryResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.InventorySlotResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.ItemResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.RecipeIngredientRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.RecipeIngredientResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.RecipeResponseDTO;
import com.RavenDev.inventory_crafting_api.exception.CraftingException;
import com.RavenDev.inventory_crafting_api.exception.ResourceNotFoundException;
import com.RavenDev.inventory_crafting_api.repository.InventoryRepository;
import com.RavenDev.inventory_crafting_api.repository.ItemRepository;
import com.RavenDev.inventory_crafting_api.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class CraftingService {

    private final RecipeRepository recipeRepository;
    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    public CraftingService(RecipeRepository recipeRepository,
                           ItemRepository itemRepository,
                           InventoryRepository inventoryRepository) {
        this.recipeRepository = recipeRepository;
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public RecipeResponseDTO createRecipe(CreateRecipeRequestDTO dto) {
        Item resultItem = itemRepository.findById(dto.resultItemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item resultante no encontrado con id: " + dto.resultItemId()));

        Recipe recipe = new Recipe();
        recipe.setName(dto.name());
        recipe.setResultItem(resultItem);
        recipe.setResultQuantity(dto.resultQuantity());

        for (RecipeIngredientRequestDTO ingDto : dto.ingredients()) {
            Item ingredientItem = itemRepository.findById(ingDto.itemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ingrediente no encontrado con id: " + ingDto.itemId()));

            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setRecipe(recipe);
            ingredient.setItem(ingredientItem);
            ingredient.setQuantity(ingDto.quantity());
            recipe.getIngredients().add(ingredient);
        }

        Recipe saved = recipeRepository.save(recipe);
        return mapToRecipeDTO(saved);
    }

    @Transactional
    public InventoryResponseDTO craft(Long inventoryId, Long recipeId) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id: " + inventoryId));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Receta no encontrada con id: " + recipeId));

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            Long requiredItemId = ingredient.getItem().getId();
            int requiredQty = ingredient.getQuantity();

            int availableQty = inventory.getSlots().stream()
                    .filter(slot -> slot.getItem().getId().equals(requiredItemId))
                    .mapToInt(InventorySlot::getQuantity)
                    .sum();

            if (availableQty < requiredQty) {
                throw new CraftingException("Materiales insuficientes: Requiere " + requiredQty +
                        " de " + ingredient.getItem().getName() + " pero solo tienes " + availableQty);
            }
        }

        for (RecipeIngredient ingredient : recipe.getIngredients()) {
            int toDeduct = ingredient.getQuantity();
            Iterator< InventorySlot > iterator = inventory.getSlots().iterator();

            while (iterator.hasNext() && toDeduct > 0) {
                InventorySlot slot = iterator.next();
                if (slot.getItem().getId().equals(ingredient.getItem().getId())) {
                    if (slot.getQuantity() <= toDeduct) {
                        toDeduct -= slot.getQuantity();
                        iterator.remove();
                    } else {
                        slot.setQuantity(slot.getQuantity() - toDeduct);
                        toDeduct = 0;
                    }
                }
            }
        }

        Item resultItem = recipe.getResultItem();
        Optional< InventorySlot > existingSlot = inventory.getSlots().stream()
                .filter(slot -> slot.getItem().getId().equals(resultItem.getId()))
                .findFirst();

        if (existingSlot.isPresent()) {
            InventorySlot slot = existingSlot.get();
            slot.setQuantity(slot.getQuantity() + recipe.getResultQuantity());
        } else {
            InventorySlot newSlot = new InventorySlot();
            newSlot.setItem(resultItem);
            newSlot.setQuantity(recipe.getResultQuantity());
            newSlot.setInventory(inventory);
            inventory.getSlots().add(newSlot);
        }

        Inventory updated = inventoryRepository.save(inventory);
        return mapToInventoryDTO(updated);
    }

    private RecipeResponseDTO mapToRecipeDTO(Recipe recipe) {
        Item res = recipe.getResultItem();
        ItemResponseDTO resultDTO = new ItemResponseDTO(res.getId(), res.getName(), res.getType(), res.getValue());

        List< RecipeIngredientResponseDTO > ingDTOs = new ArrayList<>();
        for (RecipeIngredient ing : recipe.getIngredients()) {
            Item it = ing.getItem();
            ItemResponseDTO itDTO = new ItemResponseDTO(it.getId(), it.getName(), it.getType(), it.getValue());
            ingDTOs.add(new RecipeIngredientResponseDTO(ing.getId(), itDTO, ing.getQuantity()));
        }

        return new RecipeResponseDTO(recipe.getId(), recipe.getName(), resultDTO, recipe.getResultQuantity(), ingDTOs);
    }

    private InventoryResponseDTO mapToInventoryDTO(Inventory inventory) {
        List< InventorySlotResponseDTO > slotDTOs = new ArrayList<>();
        for (InventorySlot slot : inventory.getSlots()) {
            Item item = slot.getItem();
            ItemResponseDTO itemDTO = new ItemResponseDTO(item.getId(), item.getName(), item.getType(), item.getValue());
            slotDTOs.add(new InventorySlotResponseDTO(slot.getId(), itemDTO, slot.getQuantity()));
        }
        return new InventoryResponseDTO(inventory.getId(), inventory.getOwnerName(), slotDTOs);
    }
}