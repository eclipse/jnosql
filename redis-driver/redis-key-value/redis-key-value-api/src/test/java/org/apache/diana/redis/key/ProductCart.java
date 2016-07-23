package org.apache.diana.redis.key;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductCart implements Serializable {

    private static final long serialVersionUID = 4087960613230439836L;

    private String name;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ProductCart(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public ProductCart() {
    }

}