/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.reader;


import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.ValueReader;
import org.eclipse.jnosql.communication.ValueReaderDecorator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

import static java.util.stream.StreamSupport.stream;

/**
 * The {@link TypeReferenceReader} to {@link java.util.stream.Stream}
 */
public class StreamTypeReferenceReader implements TypeReferenceReader {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public boolean test(TypeSupplier<?> typeReference) {
        Type type = typeReference.get();
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            return Stream.class.equals(parameterizedType.getRawType()) &&
                    Class.class.isInstance(parameterizedType.getActualTypeArguments()[0]);
        }
        return false;
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {
        Type type = typeReference.get();
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        Class<?> classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (T) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(classType, o));
        }
        return (T) Stream.of(SERVICE_PROVIDER.read(classType, value));
    }


}
