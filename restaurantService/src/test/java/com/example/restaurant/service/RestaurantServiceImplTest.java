package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
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
public class RestaurantServiceImplTest {

    @Mock
    private RestaurantRepository repository;

    @InjectMocks
    private RestaurantServiceImpl service;

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
        restaurant.setRestaurantId(1);
        restaurant.setName("Test Restaurant");
        restaurant.setCuisine("Italian");
        restaurant.setCity("New York");
        restaurant.setOwnerId(10);
        restaurant.setApproved(true);
        restaurant.setOpen(true);
    }

    @Test
    void testRegisterRestaurant() {
        when(repository.save(any(Restaurant.class))).thenReturn(restaurant);
        Restaurant saved = service.registerRestaurant(restaurant);
        assertNotNull(saved);
        assertEquals("Test Restaurant", saved.getName());
        verify(repository, times(1)).save(restaurant);
    }

    @Test
    void testGetById_Found() {
        when(repository.findById(1)).thenReturn(Optional.of(restaurant));
        Restaurant found = service.getById(1);
        assertNotNull(found);
        assertEquals(1, found.getRestaurantId());
    }

    @Test
    void testGetById_NotFound() {
        when(repository.findById(99)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.getById(99));
    }

    @Test
    void testGetByOwner() {
        when(repository.findByOwnerId(10)).thenReturn(Arrays.asList(restaurant));
        List<Restaurant> list = service.getByOwner(10);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
    }
}
