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


import org.eclipse.jnosql.communication.ValueReader;
import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.ValueReaderDecorator;
import org.eclipse.jnosql.communication.Entry;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * The {@link TypeReferenceReader} to {@link Map}
 */
@SuppressWarnings("unchecked")
public class MapTypeReferenceReader implements TypeReferenceReader {

    private static final ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();
    private static final Predicate<Type> IS_CLASS = c -> c instanceof Class;

    @Override
    public boolean test(TypeSupplier<?> typeReference) {
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
        Class<?> valueType = (Class<?>) Optional.of(parameterizedType.getActualTypeArguments()[1])
                .filter(IS_CLASS).orElse(Object.class);
        return (T) getMap(keyType, valueType, value);

    }

    private <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass, Object value) {

        if (Map.class.isInstance(value)) {
            return convertToMap(keyClass, valueClass, value);
        }
        if (Iterable.class.isInstance(value)) {
            List<Object> collection = new ArrayList<>();
            Iterable.class.cast(value).forEach(collection::add);
            if (collection.isEmpty()) {
                return Collections.emptyMap();
            }
            if (collection.size() == 1) {
                Object object = collection.get(0);
                if (object instanceof Map) {
                    return convertToMap(keyClass, valueClass, object);
                }
                if (object instanceof Entry) {
                    Map<String, Object> map = new HashMap<>();
                    convertEntryToMap(object, map);
                    return convertToMap(keyClass, valueClass, map);
                }
            }
        }
        throw new UnsupportedOperationException("There is not supported convert" + value + " a not Map type.");
    }

    private void convertEntryToMap(Object value, Map<String, Object> map) {
        Entry entry = Entry.class.cast(value);
        Object entryValue = entry.getValue().get();
        if (entryValue instanceof Entry) {
            Map<String, Object> subMap = new HashMap<>();
            Entry subEntry = Entry.class.cast(entryValue);
            convertEntryToMap(subEntry, subMap);
            map.put(entry.getName(), subMap);
        } else {
            map.put(entry.getName(), entryValue);
        }
    }

    private <K, V> Map<K, V> convertToMap(Class<K> keyClass, Class<V> valueClass, Object value) {
        Map mapValue = Map.class.cast(value);
        return (Map<K, V>) mapValue.keySet().stream()
                .collect(Collectors.toMap(mapKeyElement(keyClass), mapValueElement(valueClass, mapValue)));
    }

    private <K> Function mapKeyElement(Class<K> keyClass) {
        return (keyElement) -> {
            if (SERVICE_PROVIDER.test(keyClass)) {
                return SERVICE_PROVIDER.read(keyClass, keyElement);
            }
            return keyElement;
        };
    }

    private <V> Function mapValueElement(Class<V> valueClass, Map mapValue) {
        return (keyElement) -> {
            Object valueElement = mapValue.get(keyElement);
            if (SERVICE_PROVIDER.test(valueClass)) {
                return SERVICE_PROVIDER.read(valueClass, valueElement);
            }
            return valueElement;
        };
    }


}
