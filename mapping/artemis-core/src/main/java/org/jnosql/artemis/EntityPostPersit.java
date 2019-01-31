/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis;


import java.util.Objects;

/**
 * When an entity is either saved or updated it's the first event to fire after the database action.
 */
public interface EntityPostPersit {

    /**
     * Return the entity whose was either saved or updated
     *
     * @return Return the entity whose was either insert or update
     */
    Object getValue();

    /**
     * Created the default implementation of {@link EntityPostPersit}
     *
     * @param value the value
     * @return the new instance of {@link EntityPostPersit}
     * @throws NullPointerException when value is null
     */
    static EntityPostPersit of(Object value) {
        Objects.requireNonNull(value, "value is required");
        return new DefaultEntityPostPersist(value);
    }

}
