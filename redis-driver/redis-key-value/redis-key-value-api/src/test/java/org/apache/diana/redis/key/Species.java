package org.apache.diana.redis.key;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Species implements Serializable {

    private static final long serialVersionUID = -1493508757572337719L;

    private List<String> animals;

    public Species(String... animals) {
        this.animals = Arrays.asList(animals);
    }

    public List<String> getAnimals() {
        return animals;
    }
    public void setAnimals(List<String> animals) {
        this.animals = animals;
    }

}