package com.example.menu.service;

import com.example.menu.entity.MenuCategory;
import com.example.menu.entity.MenuItem;

import java.util.List;

public interface MenuService {

    MenuCategory addCategory(MenuCategory category);

    MenuItem addMenuItem(MenuItem item);

    List<MenuItem> getMenuByRestaurant(Integer restaurantId);

    List<MenuCategory> getCategoriesByRestaurant(Integer restaurantId);

    MenuItem getItemById(Integer id);

    MenuItem updateMenuItem(Integer id, MenuItem item);

    void toggleAvailability(Integer id);

    void deleteMenuItem(Integer id);

    List<MenuItem> searchMenuItems(String keyword);

    List<MenuItem> getVegItems(Integer restaurantId);
}