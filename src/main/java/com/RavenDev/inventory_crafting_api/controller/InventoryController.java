package com.RavenDev.inventory_crafting_api.controller;

import com.RavenDev.inventory_crafting_api.dto.AddItemRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.InventoryResponseDTO;
import com.RavenDev.inventory_crafting_api.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity< InventoryResponseDTO > createInventory(@RequestParam String ownerName) {
        InventoryResponseDTO created = inventoryService.createInventory(ownerName);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity< InventoryResponseDTO > getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity< InventoryResponseDTO > addItem(
            @PathVariable Long id,
            @Valid @RequestBody AddItemRequestDTO dto
    ) {
        return ResponseEntity.ok(inventoryService.addItem(id, dto));
    }
}
