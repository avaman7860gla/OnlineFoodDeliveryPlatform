package com.example.restaurant.repository;

import com.example.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByOwnerId(Integer ownerId);

    List<Restaurant> findByCuisine(String cuisine);

    List<Restaurant> findByCity(String city);

    List<Restaurant> findByIsOpenAndIsApproved(boolean isOpen, boolean isApproved);

    List<Restaurant> findByNameContainingIgnoreCase(String name);

    Optional<Restaurant> findByRestaurantId(Integer id);

    long countByCity(String city);
}