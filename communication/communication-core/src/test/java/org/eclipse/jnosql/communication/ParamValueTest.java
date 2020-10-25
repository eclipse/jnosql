/*
 *  Copyright (c) 2019 Otávio Santana and others
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

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.Value;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.*;

class ParamValueTest {




    @Test
    public void shouldAddParameter() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertNotNull(name);
        MatcherAssert.<List<String>>assertThat(params.getParametersNames(), containsInAnyOrder("name"));
    }

    @Test
    public void shouldNotUseValueWhenIsInvalid() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertThrows(QueryException.class, name::get);

        assertThrows(QueryException.class, () -> name.get(String.class));
    }

    @Test
    public void shouldSetParameter() {
        Params params = Params.newParams();
        Value name = params.add("name");

        params.bind("name", "Ada Lovelace");

        assertEquals("Ada Lovelace", name.get());
    }

    @Test
    public void shouldReturnsTrueWhenValueIsEmpty() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertTrue(name.isInstanceOf(Integer.class));
    }

    @Test
    public void shouldInstanceOf() {
        Params params = Params.newParams();
        Value name = params.add("name");
        assertTrue(name.isInstanceOf(Integer.class));

        params.bind("name", "Ada Lovelace");
        assertTrue(name.isInstanceOf(String.class));
        assertFalse(name.isInstanceOf(Integer.class));

    }
}