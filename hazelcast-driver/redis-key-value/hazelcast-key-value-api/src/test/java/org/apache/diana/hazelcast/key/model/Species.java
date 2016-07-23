package org.apache.diana.hazelcast.key.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Species implements Serializable {

    private static final long serialVersionUID = -1493508757572337719L;

    private final List<String> animals;

    public Species(String... animals) {
        this.animals = Arrays.asList(animals);
    }

    public List<String> getAnimals() {
        return animals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Species species = (Species) o;
        return Objects.equals(animals, species.animals);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(animals);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Species{");
        sb.append("animals=").append(animals);
        sb.append('}');
        return sb.toString();
    }
}