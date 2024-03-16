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
import jakarta.nosql.Embeddable;

import static jakarta.nosql.Embeddable.EmbeddableType.GROUPING;

@Embeddable(GROUPING)
public class WineFactory {

    @Column
    private String name;

    @Column
    private String location;

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "WineFactory{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public static WineFactory of(String name, String location) {
        WineFactory factory = new WineFactory();
        factory.name = name;
        factory.location = location;
        return factory;
    }

}
