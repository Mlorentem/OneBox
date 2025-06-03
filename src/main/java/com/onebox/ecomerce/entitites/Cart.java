package com.onebox.ecomerce.entitites;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Cart {
    
    @Getter @Setter
    private long id;

    @Getter @Setter
    private List<Product> products;

    public Cart(long id){
        this.id = id;
    }
}
