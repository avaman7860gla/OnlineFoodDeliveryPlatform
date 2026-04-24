package com.example.restaurant.service;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repo;

    @Override
    public Restaurant registerRestaurant(Restaurant restaurant) {
        return repo.save(restaurant);
    }

    @Override
    public Restaurant getById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }

    @Override
    public List<Restaurant> getByOwner(Integer ownerId) {
        return repo.findByOwnerId(ownerId);
    }

    @Override
    public List<Restaurant> getByCuisine(String cuisine) {
        return repo.findByCuisine(cuisine);
    }

    @Override
    public List<Restaurant> getByCity(String city) {
        return repo.findByCity(city);
    }

    @Override
    public List<Restaurant> getNearby(double lat, double lon) {
        return repo.findAll(); // simplified (geo logic later)
    }

    @Override
    public List<Restaurant> searchRestaurants(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Restaurant updateRestaurant(Integer id, Restaurant updated) {
        Restaurant r = repo.findById(id).orElseThrow();
        r.setName(updated.getName());
        r.setCuisine(updated.getCuisine());
        r.setCity(updated.getCity());
        r.setDescription(updated.getDescription());
        return repo.save(r);
    }

    @Override
    public void approveRestaurant(Integer id) {
        Restaurant r = repo.findById(id).orElseThrow();
        r.setApproved(true);
        repo.save(r);
    }

    @Override
    public void toggleOpen(Integer id) {
        Restaurant r = repo.findById(id).orElseThrow();
        r.setOpen(!r.isOpen());
        repo.save(r);
    }

    @Override
    public void deleteRestaurant(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public void updateRating(Integer id, double rating) {
        Restaurant r = repo.findById(id).orElseThrow();
        r.setAvgRating((r.getAvgRating() + rating) / 2);
        repo.save(r);
    }
    
    @Override
    public List<Restaurant> getAllRestaurants() {
        return repo.findAll();
    }
}