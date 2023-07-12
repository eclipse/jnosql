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
import java.util.Collections;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.util.stream.StreamSupport.stream;

/**
 * The {@link TypeReferenceReader} to {@link NavigableSet} and {@link SortedSet}
 */
@SuppressWarnings("unchecked")
public class NavigableSetTypeReferenceReader implements TypeReferenceReader {

    private static final ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public boolean test(TypeSupplier<?> typeReference) {
        Type type = typeReference.get();
        if (type instanceof ParameterizedType parameterizedType) {

            Type collectionType = parameterizedType.getRawType();
            Type elementType = parameterizedType.getActualTypeArguments()[0];

            boolean isNavigableSet = (NavigableSet.class.equals(collectionType)
                    ||
                    SortedSet.class.equals(collectionType));
            boolean isElementCompatible = elementType instanceof Class
                    && Comparable.class.isAssignableFrom((Class<?>) elementType);

            return isNavigableSet && isElementCompatible;
        }
        return false;
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {
        Type type = typeReference.get();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (T) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(classType, o))
                    .collect(Collectors.toCollection(TreeSet::new));
        }
        return (T) new TreeSet<>(Collections.singletonList(SERVICE_PROVIDER.read(classType, value)));
    }


}
