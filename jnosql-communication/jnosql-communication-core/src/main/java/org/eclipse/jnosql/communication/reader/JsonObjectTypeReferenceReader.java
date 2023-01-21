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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * A {@link TypeReferenceReader} that uses the return as {@link JsonObject}
 * @author Dmitry Repchevsky
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
        Iterator<Entry> entries = iterable.iterator();
        while (entries.hasNext()) {
            final Entry doc = entries.next();
            final String name = doc.name();
            final Object value = doc.value().get();
            if (Iterable.class.isInstance(value)) {
                List<?> items = (List) StreamSupport.stream(Iterable.class.cast(value).spliterator(), false)
                        .collect(Collectors.toUnmodifiableList());

                if (items.isEmpty()) {
                    builder.add(name, Json.createObjectBuilder().build());
                } else if (items.stream().anyMatch(p -> p instanceof Entry)) {
                    builder.add(name, convert(items));
                } else {
                    final JsonArrayBuilder ab = Json.createArrayBuilder();
                    for (Object item : items) {
                        JsonValue json = Iterable.class.isInstance(item) ?
                                convert(Iterable.class.cast(item)) : getJsonValue(item);
                        if (json != null) {
                            ab.add(json);
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
        } else if (value instanceof Number) {
            return Json.createValue(Number.class.cast(value));
        } else if (value instanceof Boolean) {
            return Boolean.class.cast(value) ? JsonValue.TRUE : JsonValue.FALSE;
        }
        return null;
    }
}

