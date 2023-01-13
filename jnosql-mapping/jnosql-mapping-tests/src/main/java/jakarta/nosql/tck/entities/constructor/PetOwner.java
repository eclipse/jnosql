/*
 *   Copyright (c) 2023 Contributors to the Eclipse Foundation
 *    All rights reserved. This program and the accompanying materials
 *    are made available under the terms of the Eclipse Public License v1.0
 *    and Apache License v2.0 which accompanies this distribution.
 *    The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *    and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *    You may elect to redistribute this code under either of these licenses.
 *
 *    Contributors:
 *
 *    Otavio Santana
 */
package jakarta.nosql.tck.entities.constructor;

import jakarta.nosql.mapping.Column;
import jakarta.nosql.mapping.Entity;
import jakarta.nosql.mapping.Id;
import jakarta.nosql.tck.entities.Animal;

import java.util.Objects;

@Entity
public class PetOwner {

    @Id
    private final Long id;

    @Column
    private final String name;

    @Column
    private final Animal animal;

    public PetOwner(@Id Long id, @Column("name") String name, @Column("animal") Animal animal) {
        this.id = id;
        this.name = name;
        this.animal = animal;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Animal getAnimal() {
        return animal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PetOwner petOwner = (PetOwner) o;
        return Objects.equals(id, petOwner.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PetOwner{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", animal=" + animal +
                '}';
    }
}
