/*
 *
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
 *
 */

package org.eclipse.jnosql.diana.reader;


import jakarta.nosql.ValueReader;

import java.util.Optional;

/**
 * Class to reads and converts to {@link Optional}
 */
@SuppressWarnings("unchecked")
public final class OptionalReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> clazz) {
        return Optional.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Optional.class.isInstance(value)) {
            return (T) value;
        }
        return (T) Optional.ofNullable(value);
    }


}
