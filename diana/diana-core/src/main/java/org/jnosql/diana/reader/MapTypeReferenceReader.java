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
package org.jnosql.diana.reader;


import jakarta.nosql.ValueReader;
import jakarta.nosql.TypeReferenceReader;
import jakarta.nosql.TypeSupplier;
import jakarta.nosql.ValueReaderDecorator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The {@link TypeReferenceReader} to {@link Map}
 */
@SuppressWarnings("unchecked")
public class MapTypeReferenceReader implements TypeReferenceReader {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public <T> boolean isCompatible(TypeSupplier<T> typeReference) {
        Type type = typeReference.get();
        if (ParameterizedType.class.isInstance(type)) {
            ParameterizedType parameterizedType = ParameterizedType.class.cast(type);

            return Map.class.equals(parameterizedType.getRawType()) &&
                    Class.class.isInstance(parameterizedType.getActualTypeArguments()[0])
                    && Class.class.isInstance(parameterizedType.getActualTypeArguments()[1]);
        }
        return false;
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {
        Type type = typeReference.get();
        ParameterizedType parameterizedType = ParameterizedType.class.cast(type);
        Class<?> keyType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        Class<?> valueType = (Class<?>) parameterizedType.getActualTypeArguments()[1];
        return (T) getMap(keyType, valueType, value);

    }

    private <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass, Object value) {

        if (Map.class.isInstance(value)) {
            Map mapValue = Map.class.cast(value);
            return (Map<K, V>) mapValue.keySet().stream()
                    .collect(Collectors.toMap(mapKeyElement(keyClass), mapValueElement(valueClass, mapValue)));
        }
        throw new UnsupportedOperationException("There is not supported convert" + value + " a not Map type.");
    }

    private <K> Function mapKeyElement(Class<K> keyClass) {
        return (keyElement) -> {
            if (SERVICE_PROVIDER.isCompatible(keyClass)) {
                return SERVICE_PROVIDER.read(keyClass, keyElement);
            }
            return keyElement;
        };
    }

    private <V> Function mapValueElement(Class<V> valueClass, Map mapValue) {
        return (keyElement) -> {
            Object valueElement = mapValue.get(keyElement);
            if (SERVICE_PROVIDER.isCompatible(valueClass)) {
                return SERVICE_PROVIDER.read(valueClass, valueElement);
            }
            return valueElement;
        };
    }


}
