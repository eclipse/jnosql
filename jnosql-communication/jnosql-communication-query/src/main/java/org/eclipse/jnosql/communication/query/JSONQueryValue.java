/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.StringReader;
import java.util.Objects;


/**
 * JavaScript Object Notation is a lightweight data-interchange format.
 */
public final class JSONQueryValue implements QueryValue<JsonObject> {

    private final JsonObject value;

    JSONQueryValue(JsonObject value) {
        this.value = value;
    }

    public JsonObject get() {
        return value;
    }

    @Override
    public ValueType type() {
        return ValueType.JSON;
    }

    public static JSONQueryValue of(QueryParser.JsonContext context) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(context.getText()))) {
            return new JSONQueryValue(jsonReader.readObject());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JSONQueryValue that = (JSONQueryValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "JSONQueryValue{" +
                "value=" + value +
                '}';
    }
}
