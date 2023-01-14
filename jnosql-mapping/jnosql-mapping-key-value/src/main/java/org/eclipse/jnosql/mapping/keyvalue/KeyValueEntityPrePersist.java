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
package org.eclipse.jnosql.mapping.keyvalue;

import org.eclipse.jnosql.communication.keyvalue.KeyValueEntity;

import java.util.Objects;
import java.util.function.Supplier;



/**
 * The interface represents the model before the KeyValueEntity be saved that  event will fire.
 */
public final class KeyValueEntityPrePersist implements Supplier<KeyValueEntity> {

    private final KeyValueEntity entity;

    KeyValueEntityPrePersist(KeyValueEntity entity) {
        this.entity = entity;
    }

    @Override
    public KeyValueEntity get() {
        return entity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        KeyValueEntityPrePersist that = (KeyValueEntityPrePersist) o;
        return Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(entity);
    }

    @Override
    public String toString() {
        return  "KeyValueEntityPrePersist{" + "entity=" + entity +
                '}';
    }
}
