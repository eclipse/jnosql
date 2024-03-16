package org.eclipse.jnosql.mapping.semistructured.entities.constructor;


import jakarta.nosql.Column;
import jakarta.nosql.Embeddable;

@Embeddable(Embeddable.EmbeddableType.GROUPING)
public record BeerFactory(@Column String name, @Column String location) {
    public static BeerFactory of(String name, String location) {
        return new BeerFactory(name, location);
    }
}
