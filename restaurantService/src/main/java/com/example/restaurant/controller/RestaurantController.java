package com.example.restaurant.controller;

import com.example.restaurant.entity.Restaurant;
import com.example.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    // VIEW ALL
    @GetMapping({"", "/"})
    @PreAuthorize("permitAll()")
    public List<Restaurant> getAll() {
        return service.getAllRestaurants();
    }

    // GET BY ID
    @GetMapping({"/{id}", "/{id}/"})
    @PreAuthorize("permitAll()")
    public Restaurant getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    // GET BY CITY
    @GetMapping({"/city/{city}", "/city/{city}/"})
    @PreAuthorize("permitAll()")
    public List<Restaurant> getByCity(@PathVariable String city) {
        return service.getByCity(city);
    }

    // GET BY CUISINE
    @GetMapping({"/cuisine/{cuisine}", "/cuisine/{cuisine}/"})
    @PreAuthorize("permitAll()")
    public List<Restaurant> getByCuisine(@PathVariable String cuisine) {
        return service.getByCuisine(cuisine);
    }

    // GET BY OWNER
    @GetMapping("/owner/{ownerId}")
    @PreAuthorize("hasAnyAuthority('OWNER','ADMIN')")
    public List<Restaurant> getByOwner(@PathVariable Integer ownerId) {
        return service.getByOwner(ownerId);
    }

    // SEARCH
    @GetMapping({"/search/{keyword}", "/search/{keyword}/"})
    @PreAuthorize("permitAll()")
    public List<Restaurant> search(@PathVariable String keyword) {
        return service.searchRestaurants(keyword);
    }

    // GET MY RESTAURANTS (OWNER)
    @GetMapping("/mine")
    @PreAuthorize("hasAnyAuthority('OWNER', 'PARTNER')")
    public List<Restaurant> getMyRestaurants() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return service.getByOwnerEmail(email);
    }

    // ADD RESTAURANT (OWNER)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('OWNER', 'PARTNER')")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return service.registerRestaurant(restaurant);
    }

    // UPDATE (OWNER)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'PARTNER')")
    public Restaurant update(@PathVariable Integer id, @RequestBody Restaurant r) {
        return service.updateRestaurant(id, r);
    }

    // TOGGLE OPEN (OWNER)
    @PutMapping("/toggle/{id}")
    @PreAuthorize("hasAnyAuthority('OWNER', 'PARTNER')")
    public void toggle(@PathVariable Integer id) {
        service.toggleOpen(id);
    }

    // UPDATE RATING (CUSTOMER)
    @PutMapping("/rating/{id}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','USER')")
    public void updateRating(@PathVariable Integer id, @RequestParam double rating) {
        service.updateRating(id, rating);
    }

    // APPROVE (ADMIN)
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void approve(@PathVariable Integer id) {
        service.approveRestaurant(id);
    }

    // DELETE (ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void delete(@PathVariable Integer id) {
        service.deleteRestaurant(id);
    }
}