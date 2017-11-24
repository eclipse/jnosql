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
package org.jnosql.diana.api;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.*;

public class SettingsBuilderTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenKeyIsNUll() {
        Settings.builder().put(null, "value");
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenValueIsNUll() {
        Settings.builder().put("key", null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenMapHasNullKey() {
        Map<String, Object> map = Collections.singletonMap(null, "value");
        Settings.builder().putAll(map);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenMapHasNullValue() {
        Map<String, Object> map = Collections.singletonMap("key", null);
        Settings.builder().putAll(map);
    }
}