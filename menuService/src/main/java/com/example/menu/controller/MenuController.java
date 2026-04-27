package com.example.menu.controller;

import com.example.menu.entity.MenuItem;
import com.example.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService service;

    //  OWNER
    @PostMapping
    @PreAuthorize("hasAuthority('OWNER')")
    public MenuItem addItem(@RequestBody MenuItem item) {
        return service.addMenuItem(item);
    }

    //  VIEW
    @GetMapping("/{restaurantId}")
    @PreAuthorize("hasAnyAuthority('CUSTOMER','OWNER','ADMIN')")
    public List<MenuItem> getMenu(@PathVariable Integer restaurantId) {
        return service.getMenuByRestaurant(restaurantId);
    }

    //  GET ITEM
    @GetMapping("/item/{id}")
    public MenuItem getItem(@PathVariable Integer id) {
        return service.getItemById(id);
    }

    //  UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public MenuItem update(@PathVariable Integer id, @RequestBody MenuItem item) {
        return service.updateMenuItem(id, item);
    }

    //  TOGGLE
    @PutMapping("/toggle/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public void toggle(@PathVariable Integer id) {
        service.toggleAvailability(id);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('OWNER')")
    public void delete(@PathVariable Integer id) {
        service.deleteMenuItem(id);
    }

    //  SEARCH
    @GetMapping("/search/{keyword}")
    public List<MenuItem> search(@PathVariable String keyword) {
        return service.searchMenuItems(keyword);
    }

    //  VEG ITEMS
    @GetMapping("/veg/{restaurantId}")
    public List<MenuItem> vegItems(@PathVariable Integer restaurantId) {
        return service.getVegItems(restaurantId);
    }
}