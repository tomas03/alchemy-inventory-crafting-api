package com.RavenDev.inventory_crafting_api;

import com.RavenDev.inventory_crafting_api.domain.*;
import com.RavenDev.inventory_crafting_api.dto.InventoryResponseDTO;
import com.RavenDev.inventory_crafting_api.exception.CraftingException;
import com.RavenDev.inventory_crafting_api.repository.InventoryRepository;
import com.RavenDev.inventory_crafting_api.repository.ItemRepository;
import com.RavenDev.inventory_crafting_api.repository.RecipeRepository;
import com.RavenDev.inventory_crafting_api.service.CraftingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CraftingServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private CraftingService craftingService;

    private Item mineralHierro;
    private Item espadaHierro;
    private Recipe recetaEspada;
    private Inventory inventario;

    @BeforeEach
    void setUp() {
        // Ítems base
        mineralHierro = new Item();
        mineralHierro.setId(1L);
        mineralHierro.setName("Mineral de Hierro");
        mineralHierro.setType(ItemType.Material);
        mineralHierro.setValue(10.0);
        espadaHierro = new Item();
        espadaHierro.setId(2L);
        espadaHierro.setName("Espada de Hierro");
        espadaHierro.setType(ItemType.Equipment);
        espadaHierro.setValue(100.0);

        // Receta: 5 Minerales de Hierro -> 1 Espada de Hierro
        recetaEspada = new Recipe();
        recetaEspada.setId(10L);
        recetaEspada.setName("Forjar Espada");
        recetaEspada.setResultItem(espadaHierro);
        recetaEspada.setResultQuantity(1);

        RecipeIngredient ingrediente = new RecipeIngredient();
        ingrediente.setId(100L);
        ingrediente.setItem(mineralHierro);
        ingrediente.setQuantity(5);
        ingrediente.setRecipe(recetaEspada);

        List<RecipeIngredient> ingredientes = new ArrayList<>();
        ingredientes.add(ingrediente);
        recetaEspada.setIngredients(ingredientes);

        // Inventario base vacío
        inventario = new Inventory();
        inventario.setId(1L);
        inventario.setOwnerName("Raven");
        inventario.setSlots(new ArrayList<>());
    }

    @Test
    @DisplayName("Debe craftear con éxito, descontar ingredientes y agregar ítem resultante")
    void craft_Success() {
        // Arrange: El inventario tiene 8 minerales de hierro (alcanza para la receta de 5)
        InventorySlot slotHierro = new InventorySlot();
        slotHierro.setId(50L);
        slotHierro.setItem(mineralHierro);
        slotHierro.setQuantity(8);
        slotHierro.setInventory(inventario);
        inventario.getSlots().add(slotHierro);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recetaEspada));
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        InventoryResponseDTO resultado = craftingService.craft(1L, 10L);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.slots().size(), "El inventario debe tener 2 slots (sobrante + producto nuevo)");

        // Verificamos que el slot del material se redujo de 8 a 3
        assertEquals(3, slotHierro.getQuantity());

        // Verificamos que se guardó el cambio en la base de datos
        verify(inventoryRepository, times(1)).save(inventario);
    }

    @Test
    @DisplayName("Debe lanzar CraftingException si faltan ingredientes")
    void craft_Fails_WhenNotEnoughMaterials() {
        // Arrange: Solo tiene 2 minerales de hierro y la receta pide 5
        InventorySlot slotHierro = new InventorySlot();
        slotHierro.setId(50L);
        slotHierro.setItem(mineralHierro);
        slotHierro.setQuantity(2);
        slotHierro.setInventory(inventario);
        inventario.getSlots().add(slotHierro);

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(inventario));
        when(recipeRepository.findById(10L)).thenReturn(Optional.of(recetaEspada));

        // Act & Assert
        CraftingException exception = assertThrows(CraftingException.class, () -> {
            craftingService.craft(1L, 10L);
        });

        assertTrue(exception.getMessage().contains("Materiales insuficientes"));

        // Verificamos que NUNCA se haya llamado a save si falló la validación
        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}