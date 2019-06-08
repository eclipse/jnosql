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

package org.jnosql.diana.api;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link ValueReader} supported by Diana
 * @see ValueReader
 */
public final class ValueReaderDecorator implements ValueReader {

    private static final ValueReaderDecorator INSTANCE = new ValueReaderDecorator();

    private final List<ValueReader> readers = new ArrayList<>();

    {
        ServiceLoader.load(ValueReader.class).forEach(readers::add);
    }

    public static ValueReaderDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return readers.stream().anyMatch(r -> r.isCompatible(clazz));
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        ValueReader valueReader = readers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
            () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return valueReader.read(clazz, value);
    }

    @Override
    public String toString() {
        return  "ValueReaderDecorator{" + "readers=" + readers +
                '}';
    }


}
