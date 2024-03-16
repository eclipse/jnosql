/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 */
package org.eclipse.jnosql.mapping.semistructured.entities;


import jakarta.nosql.Column;
import jakarta.nosql.Entity;
import jakarta.nosql.Id;

import java.util.Objects;

@Entity
public class Wine {

    @Id
    private String id;

    @Column
    private String name;

    @Column
    private WineFactory factory;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public WineFactory getFactory() {
        return factory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Wine wine = (Wine) o;
        return Objects.equals(id, wine.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public static Wine of(String id, String name, WineFactory factory) {
        Wine wine = new Wine();
        wine.name = name;
        wine.id = id;
        wine.factory = factory;
        return wine;
    }
}
