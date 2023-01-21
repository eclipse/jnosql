/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;
import org.eclipse.jnosql.communication.Entry;
import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.eclipse.jnosql.communication.TypeSupplier;

import java.util.Iterator;
import java.util.List;

/**
 * The {@link JsonObject} extracted from Dmitry Repchevsky(redmitry) code
 */
public class JsonObjectTypeReferenceReader implements TypeReferenceReader {

    @Override
    public boolean test(TypeSupplier<?> typeReference) {
        return JsonObject.class.equals(typeReference.get());
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object obj) {
        if (Iterable.class.isInstance(obj)) {
            return (T) convert(Iterable.class.cast(obj));
        }
        return null;
    }

    public static JsonObject convert(Iterable iterable) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        Iterator<Entry> iter = iterable.iterator();
        while (iter.hasNext()) {
            final Entry entry = iter.next();
            final String name = entry.name();
            final Object value = entry.value().get();
            if (List.class.isInstance(value)) {
                final List l = List.class.cast(value);
                if (l.isEmpty()) {
                    builder.add(name, Json.createObjectBuilder().build());
                } else if (l.stream().anyMatch(p -> p instanceof Entry)) {
                    builder.add(name, convert(l));
                } else {
                    final JsonArrayBuilder ab = Json.createArrayBuilder();
                    for (Object elem : l) {
                        JsonValue v = List.class.isInstance(elem) ?
                                convert(List.class.cast(elem)) : getJsonValue(elem);
                        if (v != null) {
                            ab.add(v);
                        }
                    }
                    builder.add(name, ab);
                }
            } else {
                builder.add(name, getJsonValue(value));
            }
        }
        return builder.build();
    }

    private static JsonValue getJsonValue(Object value) {
        if (value == null) {
            return JsonValue.NULL;
        } else if (value instanceof String) {
            return Json.createValue(value.toString());
        } else if (value instanceof Double) {
            return Json.createValue(Double.class.cast(value).doubleValue());
        } else if (value instanceof Long) {
            return Json.createValue(Long.class.cast(value).longValue());
        } else if (value instanceof Integer) {
            return Json.createValue(Integer.class.cast(value).intValue());
        } else if (value instanceof Boolean) {
            return Boolean.class.cast(value) ? JsonValue.TRUE : JsonValue.FALSE;
        }
        return null;
    }
}
