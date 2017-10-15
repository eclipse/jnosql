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
package org.jnosql.diana.api.key;

import org.hamcrest.Matchers;
import org.jnosql.diana.api.TypeReference;
import org.junit.Test;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


public class DefaultKeyValueEntityTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenKeyIsNull() {
        KeyValueEntity.of(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenValueIsNull() {
        KeyValueEntity.of("key", null);
    }

    @Test
    public void shouldCreateInstance() {
        KeyValueEntity<String> entity = KeyValueEntity.of("key", "value");
        assertNotNull(entity);
        assertEquals("key", entity.getKey());
        assertEquals("value", entity.get());
    }

    @Test
    public void shouldAliasOnValue() {
        String value = "10";
        KeyValueEntity<String> entity = KeyValueEntity.of("key", value);
        assertEquals(value, entity.get());
        assertEquals(Integer.valueOf(10), entity.get(Integer.class));
        assertThat(singletonList(10), Matchers.contains(entity.get(new TypeReference<List<Integer>>() {}).get(0)));
    }
}