/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.aphrodite.antlr;

import org.jnosql.query.JSONValue;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;
import java.util.Objects;

final class DefaultJSONValue implements JSONValue {

    private final JsonObject value;

    private DefaultJSONValue(JsonObject value) {
        this.value = value;
    }

    @Override
    public JsonObject get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultJSONValue)) {
            return false;
        }
        DefaultJSONValue that = (DefaultJSONValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public static JSONValue of(QueryParser.JsonContext context) {
        JsonReader jsonReader = Json.createReader(new StringReader(context.getText()));
        return new DefaultJSONValue(jsonReader.readObject());
    }


}
