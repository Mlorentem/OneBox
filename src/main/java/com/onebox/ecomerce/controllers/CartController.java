package com.onebox.ecomerce.controllers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onebox.ecomerce.entitites.Cart;
import com.onebox.ecomerce.entitites.Product;
import com.onebox.ecomerce.errors.CartNotFoundException;
import com.onebox.ecomerce.schedulers.CartScheduler;
import com.onebox.ecomerce.services.CartService;

@RestController
@RequestMapping("/api/cart/")
public class CartController {

    private CartService cartService;
    private CartScheduler cartScheduler;

    CartController(CartService cartService, CartScheduler cartScheduler) {
        this.cartService = cartService;
        this.cartScheduler = cartScheduler;
    }

    /*
     * Create a new empty cart and schedules it for deletion after 10 minutes.
     */
    @PostMapping("")
    public ResponseEntity<?> creatyeCart() {
        Cart cart = cartService.createCart();
        cartScheduler.scheduleCartDeletion(cart.getId(), () -> cartService.deleteCart(cart.getId()), 2,
                TimeUnit.MINUTES);
        return new ResponseEntity<Cart>(cart, HttpStatus.CREATED);
    }

    /*
     * Get cart with provided id and also refreshes the timer for the scheduled deletion.
     */
    @GetMapping("{cartId}")
    public ResponseEntity<?> getCartWithId(@PathVariable Long cartId) throws CartNotFoundException {
        cartScheduler.interactWithCart(cartId);
        try {
            return new ResponseEntity<Cart>(cartService.getCart(cartId), HttpStatus.FOUND);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /*
     * Delete cart with provided id and also removes the scheduled task related.
     */
    @DeleteMapping("{cartId}")
    public ResponseEntity<?> deleteCartWithId(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        cartScheduler.deleteTaskCart(cartId);
        return new ResponseEntity<String>("Cart deleted successfully", HttpStatus.OK);
    }

    /*
     * Retrieve all the products in the cart with provided id.
     */
    @GetMapping("{cartId}/products")
    public ResponseEntity<?> getMethodName(@PathVariable Long cartId) throws CartNotFoundException {
        return new ResponseEntity<List<Product>>(cartService.getCart(cartId).getProducts(), HttpStatus.FOUND);
    }

    /*
     * Add N products to the cart with provided id.
     * The products are provided as a list of objects in like [{id: 1, description: "description", amount: 10}]
     */
    @PutMapping("{cartId}/add-products")
    public ResponseEntity<?> addProductsToMap(@PathVariable Long cartId,
            @RequestBody List<Product> productList) throws CartNotFoundException {
        System.out.println("Products: " + productList);
        try {
            return new ResponseEntity<Cart>(cartService.addProductsToCart(cartId, productList), HttpStatus.OK);
        } catch (CartNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }
}
