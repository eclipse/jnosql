/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.reader;

import org.jnosql.diana.api.ValueReader;
import org.junit.Before;
import org.junit.Test;

import java.time.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class YearReaderTest {

    private ValueReader valueReader;

    @Before
    public void init() {
        valueReader = new YearValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(Year.class));
        assertFalse(valueReader.isCompatible(String.class));
        assertFalse(valueReader.isCompatible(Long.class));
    }

    @Test
    public void shouldConvert() {
        Year year = Year.parse("2009");

        assertEquals(year, valueReader.read(Year.class, Year.parse("2009")));
        assertEquals(year, valueReader.read(String.class, "2009"));
        assertEquals(year, valueReader.read(Integer.class, 2009));
        assertEquals(year, valueReader.read(Long.class, 2009));
    }


}
