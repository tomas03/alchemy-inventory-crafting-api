package com.RavenDev.inventory_crafting_api.service;

import com.RavenDev.inventory_crafting_api.domain.Inventory;
import com.RavenDev.inventory_crafting_api.domain.InventorySlot;
import com.RavenDev.inventory_crafting_api.domain.Item;
import com.RavenDev.inventory_crafting_api.dto.AddItemRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.InventoryResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.InventorySlotResponseDTO;
import com.RavenDev.inventory_crafting_api.dto.ItemResponseDTO;
import com.RavenDev.inventory_crafting_api.exception.ResourceNotFoundException;
import com.RavenDev.inventory_crafting_api.repository.InventoryRepository;
import com.RavenDev.inventory_crafting_api.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;

    public InventoryService(InventoryRepository inventoryRepository, ItemRepository itemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
    }

    @Transactional
    public InventoryResponseDTO createInventory(String ownerName) {
        Inventory inventory = new Inventory();
        inventory.setOwnerName(ownerName);
        Inventory saved = inventoryRepository.save(inventory);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public InventoryResponseDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id: " + id));
        return mapToDTO(inventory);
    }

    @Transactional
    public InventoryResponseDTO addItem(Long inventoryId, AddItemRequestDTO dto) {
        Inventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario no encontrado con id: " + inventoryId));

        Item item = itemRepository.findById(dto.itemId())
                .orElseThrow(() -> new ResourceNotFoundException("Item no encontrado con id: " + dto.itemId()));

        Optional< InventorySlot > existingSlot = inventory.getSlots().stream()
                .filter(slot -> slot.getItem().getId().equals(item.getId()))
                .findFirst();

        if (existingSlot.isPresent()) {
            InventorySlot slot = existingSlot.get();
            slot.setQuantity(slot.getQuantity() + dto.quantity());
        } else {
            InventorySlot newSlot = new InventorySlot();
            newSlot.setItem(item);
            newSlot.setQuantity(dto.quantity());
            newSlot.setInventory(inventory);
            inventory.getSlots().add(newSlot);
        }

        Inventory updated = inventoryRepository.save(inventory);
        return mapToDTO(updated);
    }

    private InventoryResponseDTO mapToDTO(Inventory inventory) {
        List< InventorySlotResponseDTO > slotDTOs = new ArrayList<>();

        for (InventorySlot slot : inventory.getSlots()) {
            Item item = slot.getItem();
            ItemResponseDTO itemDTO = new ItemResponseDTO(
                    item.getId(),
                    item.getName(),
                    item.getType(),
                    item.getValue()
            );

            slotDTOs.add(new InventorySlotResponseDTO(
                    slot.getId(),
                    itemDTO,
                    slot.getQuantity()
            ));
        }

        return new InventoryResponseDTO(
                inventory.getId(),
                inventory.getOwnerName(),
                slotDTOs
        );
    }
}