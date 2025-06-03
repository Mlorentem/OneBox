package com.onebox.ecomerce.errors;

public class CartNotFoundException extends RuntimeException{
    public CartNotFoundException(String errorString) {
        super(errorString);
    }

    public CartNotFoundException() {
        super("Cart not found");
    }

}
