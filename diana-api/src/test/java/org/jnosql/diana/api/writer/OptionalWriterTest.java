/*
 * Copyright 2017 Eclipse Foundation
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

package org.jnosql.diana.api.writer;

import org.jnosql.diana.api.ValueWriter;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;


public class OptionalWriterTest {

    private ValueWriter<Optional, String> valueWriter;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        valueWriter = new OptionalValueWriter();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(valueWriter.isCompatible(Optional.class));
        assertFalse(valueWriter.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        String diana = "diana";
        Optional<String> optinal = Optional.of(diana);
        String result = valueWriter.write(optinal);
        assertEquals(diana, result);
    }
}