package com.RavenDev.inventory_crafting_api.service;


import com.RavenDev.inventory_crafting_api.domain.Item;
import com.RavenDev.inventory_crafting_api.dto.ItemRequestDTO;
import com.RavenDev.inventory_crafting_api.dto.ItemResponseDTO;
import com.RavenDev.inventory_crafting_api.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<ItemResponseDTO> getAllItems(){
        List<Item> items = itemRepository.findAll();
        List<ItemResponseDTO> responseList = new ArrayList<>();
        for(Item item  : items){
            ItemResponseDTO dto = new ItemResponseDTO(
                    item.getId(),
                    item.getName(),
                    item.getType(),
                    item.getValue()
            );
            responseList.add(dto);
        }
        return responseList;
    }

    public ItemResponseDTO getItemByID(Long id){
        Item item = itemRepository.findById(id).orElseThrow(()-> new RuntimeException("Item no encontrado con el id: "+id));
        return new ItemResponseDTO(
                item.getId(),
                item.getName(),
                item.getType(),
                item.getValue()
        );
    }

    public ItemResponseDTO updateItem(Long id, ItemRequestDTO dto){
        Item item = itemRepository.findById(id).orElseThrow(()-> new RuntimeException("Item no encontrado con el id: "+id));
        item.setName(dto.name());
        item.setType(dto.type());
        item.setValue(dto.value());
        Item updatedItem = itemRepository.save(item);
        return new ItemResponseDTO(
                updatedItem.getId(),
                updatedItem.getName(),
                updatedItem.getType(),
                updatedItem.getValue()
        );
    }

    public void deleteItem(Long id){
        if (!itemRepository.existsById(id)){
            throw new RuntimeException("Item no encontrado con id: "+id);
        }
        itemRepository.deleteById(id);
    }

    public ItemResponseDTO createItem(ItemRequestDTO dto){
        Item item = new Item();
        item.setName(dto.name());
        item.setType(dto.type());
        item.setValue(dto.value());
        Item savedItem = itemRepository.save(item);
        return new ItemResponseDTO(
                savedItem.getId(),
                savedItem.getName(),
                savedItem.getType(),
                savedItem.getValue()
        );
    }
}
