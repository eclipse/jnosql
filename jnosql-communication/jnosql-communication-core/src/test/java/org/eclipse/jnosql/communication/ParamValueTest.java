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
package org.eclipse.jnosql.communication;

import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParamValueTest extends ValueTest {

    @Override
    Value getValueOf(Supplier<?> supplier) {
        Params params = Params.newParams();
        Value value = params.add("name");
        params.bind("name", supplier.get());
        return value;
    }

    @Test
    void shouldAddParameter() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertNotNull(name);

        assertThat(params.getParametersNames()).contains("name");
    }

    @Test
    void shouldNotUseValueWhenIsInvalid() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertThrows(QueryException.class, name::get);

        assertThrows(QueryException.class, () -> name.get(String.class));
    }

    @Test
    void shouldSetParameter() {
        Params params = Params.newParams();
        Value name = params.add("name");

        params.bind("name", "Ada Lovelace");

        assertEquals("Ada Lovelace", name.get());
    }

    @Test
    void shouldReturnsTrueWhenValueIsEmpty() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertTrue(name.isInstanceOf(Integer.class));
    }

    @Test
    void shouldInstanceOf() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertTrue(name.isInstanceOf(Integer.class));

        params.bind("name", "Ada Lovelace");
        assertTrue(name.isInstanceOf(String.class));
        assertFalse(name.isInstanceOf(Integer.class));

    }

    @Test
    void testToString() {

        String paramName = "name";
        String paramValue = "12";

        Params params = Params.newParams();
        Value value = params.add(paramName);

        assertEquals(paramName + "= ?", value.toString());

        params.bind(paramName, paramValue);
        assertEquals(paramName + "= " + paramValue, value.toString());

    }

}