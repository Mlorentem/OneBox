package com.onebox.ecomerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.onebox.ecomerce.entitites.Cart;
import com.onebox.ecomerce.entitites.Product;
import com.onebox.ecomerce.errors.CartNotFoundException;

@SpringBootTest
public class CartServiceTests {
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartService = new CartService();
    }

    @Test
    void whenCreateCartIsCalled_CreateNewCartWithEmptyProducts() {
        Cart cart = cartService.createCart();
        assertNotNull(cart);
        assertNotNull(cart.getProducts());
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void whenGetCartIsCalled_AndIsFound() throws CartNotFoundException {
        Cart created = cartService.createCart();
        Cart fetched = cartService.getCart(created.getId());
        assertEquals(created.getId(), fetched.getId());
    }

    @Test
    void whenGetCartIsCalled_AndIsNotFound() {
        assertThrows(CartNotFoundException.class, () -> cartService.getCart(999L));
    }

    @Test
    void whenAddProductsIsCalled_ProductsAreAddedSuccessfully() throws CartNotFoundException {
        Cart cart = cartService.createCart();
        Product p1 = new Product(1L, "desc1", 2);
        Product p2 = new Product(2L, "desc2", 3);
        List<Product> products = Arrays.asList(p1, p2);

        Cart updated = cartService.addProductsToCart(cart.getId(), products);
        assertEquals(2, updated.getProducts().size());
        assertTrue(updated.getProducts().contains(p1));
        assertTrue(updated.getProducts().contains(p2));
    }

    @Test
    void whenAddProductsIsCalled_ThrowsCartNotFound() {
        Product p1 = new Product(1L, "desc1", 2);
        List<Product> products = Arrays.asList(p1);
        assertThrows(CartNotFoundException.class, () -> cartService.addProductsToCart(999L, products));
    }

    @Test
    void whenDeleteCartIsCalled_CartIsRemoved() throws CartNotFoundException {
        Cart cart = cartService.createCart();
        cartService.deleteCart(cart.getId());
        assertThrows(CartNotFoundException.class, () -> cartService.getCart(cart.getId()));
    }
}
