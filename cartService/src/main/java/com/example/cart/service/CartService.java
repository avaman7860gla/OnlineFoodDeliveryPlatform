package com.example.cart.service;

import com.example.cart.entity.Cart;

import java.util.List;

public interface CartService {

    Cart getCartByCustomer(Integer customerId);

    Cart addItem(Integer customerId, Integer menuItemId, int quantity, String customization);

    Cart removeItem(Integer customerId, Integer itemId);

    Cart updateQuantity(Integer customerId, Integer itemId, int quantity);

    void clearCart(Integer customerId);

    double cartTotal(Integer customerId);

    Cart changeRestaurant(Integer customerId, Integer restaurantId);

    Cart applyPromoCode(Integer customerId, String promo);

    List<Cart> getAllCarts();
}