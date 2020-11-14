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
package org.eclipse.jnosql.mapping;


import jakarta.nosql.mapping.EntityPostPersit;

import java.util.Objects;

/**
 * The default implementation to {@link EntityPostPersit}
 */
public final class DefaultEntityPostPersist implements EntityPostPersit {

    private final Object value;

    public DefaultEntityPostPersist(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultEntityPostPersist)) {
            return false;
        }
        DefaultEntityPostPersist that = (DefaultEntityPostPersist) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return  "DefaultEntityPostPersist{" + "value=" + value +
                '}';
    }
}
