/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph;

import java.util.Objects;

/**
 * When an entity is either saved or updated it's the first event to fire
 */
public interface EntityGraphPrePersist {


    /**
     * Return the entity whose gonna be either saved or updated
     *
     * @return Return the entity whose gonna be either insert or update
     */
    Object getValue();

    /**
     * Created the default implementation of
     *
     * @param value the value
     * @return the new instance of
     * @throws NullPointerException when value is null
     */
    static EntityGraphPrePersist of(Object value) {
        Objects.requireNonNull(value, "value is required");
        return new DefaultEntityGraphPrePersist(value);
    }

}
