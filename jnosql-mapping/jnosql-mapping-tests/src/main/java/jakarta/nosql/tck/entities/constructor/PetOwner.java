/*
 * Copyright (c) 2022 Contributors to the Eclipse Foundation
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License v. 2.0 are satisfied: GNU General Public License v2.0
 * w/Classpath exception which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
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
