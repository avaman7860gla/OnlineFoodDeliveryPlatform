package com.example.menu.repository;

import com.example.menu.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<MenuItem, Integer> {

    List<MenuItem> findByRestaurantId(Integer restaurantId);

    List<MenuItem> findByCategoryId(Integer categoryId);

    Optional<MenuItem> findByItemId(Integer itemId);

    List<MenuItem> findByIsVeg(boolean isVeg);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByPriceLessThanEqual(double price);

    List<MenuItem> findByIsAvailable(boolean isAvailable);

    long countByRestaurantId(Integer restaurantId);
}