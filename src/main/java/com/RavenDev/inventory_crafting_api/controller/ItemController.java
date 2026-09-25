package com.RavenDev.inventory_crafting_api.controller;


import com.RavenDev.inventory_crafting_api.dto.ItemRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.ItemResponseDTO;
import com.RavenDev.inventory_crafting_api.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemResponseDTO>> getAllItems(){
        return ResponseEntity.ok(itemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity getItemById(@PathVariable Long id){
        return ResponseEntity.ok(itemService.getItemByID(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemRequestDTO dto
    ){
        return ResponseEntity.ok(itemService.updateItem(id,dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> createItem(@Valid @RequestBody ItemRequestDTO dto){
        ItemResponseDTO created = itemService.createItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
