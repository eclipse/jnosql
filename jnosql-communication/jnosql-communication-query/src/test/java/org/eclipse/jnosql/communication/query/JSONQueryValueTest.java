/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JSONQueryValueTest {

    private JsonObject json;

    @BeforeEach
    public void setUp() {
        this.json = JsonObject.EMPTY_JSON_OBJECT.asJsonObject();
    }


    @Test
    public void shouldGetType() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertThat(queryValue).isNotNull()
                .extracting(JSONQueryValue::type)
                .isEqualTo(ValueType.JSON);
    }

    @Test
    public void shouldGet() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertThat(queryValue).isNotNull()
                .extracting(JSONQueryValue::get)
                .isEqualTo(json);
    }

    @Test
    public void shouldEquals(){
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertEquals(queryValue, new JSONQueryValue(json));
    }

    @Test
    public void shouldHashCode() {
        JSONQueryValue queryValue = new JSONQueryValue(json);
        assertEquals(queryValue.hashCode(), new JSONQueryValue(json).hashCode());
    }

}