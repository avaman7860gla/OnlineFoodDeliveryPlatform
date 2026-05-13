package com.example.cart.service;

import com.example.cart.entity.Cart;
import com.example.cart.entity.CartItem;
import com.example.cart.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceImplTest {

    @Mock
    private CartRepository repository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CartServiceImpl service;

    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        cartItem = new CartItem();
        cartItem.setItemId(101);
        cartItem.setMenuItemId(1);
        cartItem.setName("Burger");
        cartItem.setPrice(10.0);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setCartId(1);
        cart.setCustomerId(5);
        cart.setRestaurantId(20);
        cart.setItems(new ArrayList<>(Arrays.asList(cartItem)));
        cart.setTotalPrice(20.0);
    }

    @Test
    void testGetCartByCustomer_Exists() {
        when(repository.findByCustomerId(5)).thenReturn(Optional.of(cart));
        Cart found = service.getCartByCustomer(5);
        assertNotNull(found);
        assertEquals(5, found.getCustomerId());
    }

    @Test
    void testGetCartByCustomer_NewCart() {
        when(repository.findByCustomerId(99)).thenReturn(Optional.empty());
        when(repository.save(any(Cart.class))).thenAnswer(i -> i.getArguments()[0]);
        
        Cart found = service.getCartByCustomer(99);
        assertNotNull(found);
        assertEquals(99, found.getCustomerId());
    }

    @Test
    void testUpdateQuantity() {
        when(repository.findByCustomerId(5)).thenReturn(Optional.of(cart));
        when(repository.save(any(Cart.class))).thenReturn(cart);

        Cart updated = service.updateQuantity(5, 101, 3);
        
        assertEquals(30.0, updated.getTotalPrice());
        assertEquals(3, updated.getItems().get(0).getQuantity());
        verify(repository, times(1)).save(cart);
    }

    @Test
    void testClearCart() {
        doNothing().when(repository).deleteByCustomerId(5);
        service.clearCart(5);
        verify(repository, times(1)).deleteByCustomerId(5);
    }
}
