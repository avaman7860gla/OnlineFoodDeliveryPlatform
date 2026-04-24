package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;

import java.util.List;

public interface RestaurantService {

    Restaurant registerRestaurant(Restaurant restaurant);

    Restaurant getById(Integer id);

    List<Restaurant> getByOwner(Integer ownerId);

    List<Restaurant> getByCuisine(String cuisine);

    List<Restaurant> getByCity(String city);

    List<Restaurant> getNearby(double lat, double lon);

    List<Restaurant> searchRestaurants(String keyword);

    Restaurant updateRestaurant(Integer id, Restaurant restaurant);

    void approveRestaurant(Integer id);

    void toggleOpen(Integer id);

    void deleteRestaurant(Integer id);

    void updateRating(Integer id, double rating);
    
    List<Restaurant> getAllRestaurants();
}