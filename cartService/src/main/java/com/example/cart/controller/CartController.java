package com.example.cart.controller;

import com.example.cart.entity.Cart;
import com.example.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService service;

    @GetMapping("/{customerId}")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Cart getCart(@PathVariable Integer customerId) {
        return service.getCartByCustomer(customerId);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Cart addItem(@RequestBody Map<String, Object> req) {

        return service.addItem(
                (Integer) req.get("customerId"),
                (Integer) req.get("menuItemId"),
                (Integer) req.get("quantity"),
                (String) req.get("customization")
        );
    }

    @PutMapping("/update")
    public Cart updateQty(@RequestBody Map<String, Integer> req) {

        return service.updateQuantity(
                req.get("customerId"),
                req.get("itemId"),
                req.get("quantity")
        );
    }

    @DeleteMapping("/remove")
    public Cart remove(@RequestBody Map<String, Integer> req) {

        return service.removeItem(
                req.get("customerId"),
                req.get("itemId")
        );
    }

    @DeleteMapping("/clear/{customerId}")
    public void clear(@PathVariable Integer customerId) {
        service.clearCart(customerId);
    }

    @PutMapping("/promo")
    public Cart applyPromo(@RequestBody Map<String, String> req) {
        return service.applyPromoCode(
                Integer.valueOf(req.get("customerId")),
                req.get("promo")
        );
    }

    @GetMapping("/all")
    public List<Cart> all() {
        return service.getAllCarts();
    }
}