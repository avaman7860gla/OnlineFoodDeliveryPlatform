package com.example.cart.service;

import com.example.cart.dto.MenuItem;
import com.example.cart.entity.*;
import com.example.cart.repository.CartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository repo;
    private final RestTemplate restTemplate;

    @Override
    public Cart getCartByCustomer(Integer customerId) {
        return repo.findByCustomerId(customerId)
        		.orElseGet(() -> repo.save(new Cart(null, customerId, null, 0.0, new ArrayList<>())));
    }

    @Override
    public Cart addItem(Integer customerId, Integer menuItemId, int quantity, String customization) {

        Cart cart = getCartByCustomer(customerId);

        // 🔥 CALL MENU SERVICE
        String url = "http://localhost:8083/menu/item/" + menuItemId;
        MenuItem menu = restTemplate.getForObject(url, MenuItem.class);

        if (menu == null) {
            throw new RuntimeException("Menu item not found");
        }

        // 🔥 SET RESTAURANT ID (CRITICAL FIX)
        if (cart.getRestaurantId() == null) {
            cart.setRestaurantId(menu.getRestaurantId());
        } else if (!cart.getRestaurantId().equals(menu.getRestaurantId())) {
            throw new RuntimeException("Cannot add items from different restaurant");
        }

        // 🔥 CREATE ITEM FROM REAL DATA
        CartItem item = new CartItem();
        item.setMenuItemId(menuItemId);
        item.setName(menu.getName());
        item.setPrice(menu.getPrice());
        item.setQuantity(quantity);
        item.setCustomization(customization);

        cart.getItems().add(item);

        cart.setTotalPrice(cartTotalInternal(cart));

        return repo.save(cart);
    }

    @Override
    public Cart removeItem(Integer customerId, Integer itemId) {

        Cart cart = getCartByCustomer(customerId);

        cart.getItems().removeIf(i -> i.getItemId().equals(itemId));

        cart.setTotalPrice(cartTotalInternal(cart));

        return repo.save(cart);
    }

    @Override
    public Cart updateQuantity(Integer customerId, Integer itemId, int quantity) {

        Cart cart = getCartByCustomer(customerId);

        for (CartItem item : cart.getItems()) {
            if (item.getItemId().equals(itemId)) {
                item.setQuantity(quantity);
            }
        }

        cart.setTotalPrice(cartTotalInternal(cart));

        return repo.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Integer customerId) {
        repo.deleteByCustomerId(customerId);
    }

    @Override
    public double cartTotal(Integer customerId) {
        Cart cart = getCartByCustomer(customerId);
        return cartTotalInternal(cart);
    }

    private double cartTotalInternal(Cart cart) {
        return cart.getItems()
                .stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
    }

    @Override
    public Cart changeRestaurant(Integer customerId, Integer restaurantId) {

        Cart cart = getCartByCustomer(customerId);

        cart.setRestaurantId(restaurantId);
        cart.setItems(new ArrayList<>());

        return repo.save(cart);
    }

    @Override
    public Cart applyPromoCode(Integer customerId, String promo) {

        Cart cart = getCartByCustomer(customerId);

        cart.setTotalPrice(cart.getTotalPrice() * 0.9); // dummy 10% discount

        return repo.save(cart);
    }

    @Override
    public List<Cart> getAllCarts() {
        return repo.findAll();
    }
}