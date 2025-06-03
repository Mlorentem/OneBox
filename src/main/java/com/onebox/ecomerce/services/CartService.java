package com.onebox.ecomerce.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.onebox.ecomerce.entitites.Cart;
import com.onebox.ecomerce.entitites.Product;
import com.onebox.ecomerce.errors.CartNotFoundException;

@Service
public class CartService {

    private final Map<Long, Cart> cartMap = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    public Cart createCart() {
        long id = idGenerator.incrementAndGet();
        Cart cart = new Cart(id, new ArrayList<>());

        cartMap.put(id, cart);
        return cart;
    }

    public Cart getCart(long cartId) throws CartNotFoundException {
        if (cartMap.containsKey(cartId)) {
            return cartMap.get(cartId);
        }
        throw new CartNotFoundException("Cart not found");
    }

    public Cart addProductsToCart(long cartId, List<Product> products) throws CartNotFoundException {

        if (cartMap.containsKey(cartId)) {
            Cart cart = cartMap.get(cartId);
            cart.getProducts().addAll(products);
            return cart;
        }
        throw new CartNotFoundException("Cart not found");
    }

    public void deleteCart(long cartId) {
        cartMap.remove(cartId);
    }

}
