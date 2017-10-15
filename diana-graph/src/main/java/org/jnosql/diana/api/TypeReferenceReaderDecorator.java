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
 * Decorators of all {@link TypeReferenceReader} supported by Diana
 *
 * @see ValueReader
 */
public final class TypeReferenceReaderDecorator implements TypeReferenceReader {

    private static final TypeReferenceReaderDecorator INSTANCE = new TypeReferenceReaderDecorator();

    private final List<TypeReferenceReader> readers = new ArrayList<>();

    {
        ServiceLoader.load(TypeReferenceReader.class).forEach(readers::add);
    }

    public static TypeReferenceReaderDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public <T> boolean isCompatible(TypeSupplier<T> type) {
        return readers.stream().anyMatch(r -> r.isCompatible(type));
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {

        TypeReferenceReader valueReader = readers.stream().filter(r -> r.isCompatible(typeReference)).findFirst().
                orElseThrow(() -> new UnsupportedOperationException("The type " + typeReference + " is not supported yet"));
        return valueReader.convert(typeReference, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TypeReferenceReaderDecorator{");
        sb.append("readers=").append(readers);
        sb.append('}');
        return sb.toString();
    }


}
