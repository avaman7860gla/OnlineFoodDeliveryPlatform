package com.example.menu.service;

import com.example.menu.entity.*;
import com.example.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repo;

    @Override
    public MenuCategory addCategory(MenuCategory category) {
        return null; // optional separate repo (keep simple for now)
    }

    @Override
    public MenuItem addMenuItem(MenuItem item) {
        return repo.save(item);
    }

    @Override
    public List<MenuItem> getMenuByRestaurant(Integer restaurantId) {
        return repo.findByRestaurantId(restaurantId);
    }

    @Override
    public List<MenuCategory> getCategoriesByRestaurant(Integer restaurantId) {
        return null; // optional enhancement
    }

    @Override
    public MenuItem getItemById(Integer id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));
    }

    @Override
    public MenuItem updateMenuItem(Integer id, MenuItem updated) {
    	MenuItem item = getItemById(id);

        if (updated.getName() != null)
            item.setName(updated.getName());

        if (updated.getDescription() != null)
            item.setDescription(updated.getDescription());

        if (updated.getPrice() != 0)
            item.setPrice(updated.getPrice());
        
        System.out.println("UPDATE DATA: " + updated);

        return repo.save(item);
    }

    @Override
    public void toggleAvailability(Integer id) {
        MenuItem item = getItemById(id);
        item.setAvailable(!item.isAvailable());
        repo.save(item);
    }

    @Override
    public void deleteMenuItem(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public List<MenuItem> searchMenuItems(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<MenuItem> getVegItems(Integer restaurantId) {
        return repo.findByRestaurantId(restaurantId)
                .stream()
                .filter(MenuItem::isVeg)
                .toList();
    }
}