package com.onebox.ecomerce.entitites;

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
public class Product {
    
    @Getter @Setter
    private long id;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private int amount;
}
