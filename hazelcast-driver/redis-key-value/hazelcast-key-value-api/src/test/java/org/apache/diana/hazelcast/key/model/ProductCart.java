package org.apache.diana.hazelcast.key.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class ProductCart implements Serializable {

    private static final long serialVersionUID = 4087960613230439836L;

    private final String name;

    private final BigDecimal price;

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }


    public ProductCart(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductCart that = (ProductCart) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProductCart{");
        sb.append("name='").append(name).append('\'');
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }
}