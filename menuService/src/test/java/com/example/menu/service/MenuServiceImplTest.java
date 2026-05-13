package com.example.menu.service;

import com.example.menu.entity.MenuItem;
import com.example.menu.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {

    @Mock
    private MenuRepository repository;

    @InjectMocks
    private MenuServiceImpl service;

    private MenuItem item;

    @BeforeEach
    void setUp() {
        item = new MenuItem();
        item.setItemId(1);
        item.setName("Burger");
        item.setDescription("Delicious burger");
        item.setPrice(9.99);
        item.setRestaurantId(5);
        item.setVeg(false);
        item.setAvailable(true);
    }

    @Test
    void testAddMenuItem() {
        when(repository.save(any(MenuItem.class))).thenReturn(item);
        MenuItem saved = service.addMenuItem(item);
        assertNotNull(saved);
        assertEquals("Burger", saved.getName());
    }

    @Test
    void testGetMenuByRestaurant() {
        when(repository.findByRestaurantId(5)).thenReturn(Arrays.asList(item));
        List<MenuItem> items = service.getMenuByRestaurant(5);
        assertEquals(1, items.size());
        assertEquals("Burger", items.get(0).getName());
    }

    @Test
    void testToggleAvailability() {
        when(repository.findById(1)).thenReturn(Optional.of(item));
        when(repository.save(any(MenuItem.class))).thenReturn(item);
        
        service.toggleAvailability(1);
        assertFalse(item.isAvailable()); // It was true, should toggle to false
    }

    @Test
    void testGetVegItems() {
        MenuItem vegItem = new MenuItem();
        vegItem.setVeg(true);
        when(repository.findByRestaurantId(5)).thenReturn(Arrays.asList(item, vegItem));
        
        List<MenuItem> vegItems = service.getVegItems(5);
        assertEquals(1, vegItems.size());
        assertTrue(vegItems.get(0).isVeg());
    }
}
