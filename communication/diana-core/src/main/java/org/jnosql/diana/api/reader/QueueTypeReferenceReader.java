/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.diana.api.reader;


import org.jnosql.diana.api.TypeReferenceReader;
import org.jnosql.diana.api.TypeSupplier;
import org.jnosql.diana.api.ValueReader;
import org.jnosql.diana.api.ValueReaderDecorator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

/**
 * The {@link TypeReferenceReader} to {@link Queue}
 */
@SuppressWarnings("unchecked")
public class QueueTypeReferenceReader implements TypeReferenceReader {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public <T> boolean isCompatible(TypeSupplier<T> typeReference) {
        Type type = typeReference.get();
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            return (Queue.class.equals(parameterizedType.getRawType())
                    || Iterable.class.equals(parameterizedType.getRawType())) &&
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
            return (T) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(classType, o))
                    .collect(Collectors.toCollection(LinkedList::new));
        }
        return (T) new LinkedList<>(Collections.singletonList(SERVICE_PROVIDER.read(classType, value)));
    }


}
