/*
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

import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.TypeReferenceReader;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MapTypeReferenceReaderTest {

    private TypeReferenceReader referenceReader = new MapTypeReferenceReader();


    @Test
    public void shouldBeCompatible() {

        assertTrue(referenceReader.isCompatible(new TypeReference<Map<String, String>>(){}));
        assertTrue(referenceReader.isCompatible(new TypeReference<Map<Long, Integer>>(){}));

    }


    @Test
    public void shouldNotBeCompatible() {
        assertFalse(referenceReader.isCompatible(new TypeReference<ArrayList<BigDecimal>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<String>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Set<String>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<List<List<String>>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Queue<String>>(){}));
        assertFalse(referenceReader.isCompatible(new TypeReference<Map<Integer, List<String>>>(){}));
    }


    @Test
    public void shouldConvert() {
        assertEquals(singletonMap("123", "123"), referenceReader.convert(new TypeReference<Map<String, String>>(){}, singletonMap(123, 123L)));
        assertEquals(singletonMap(123L, 123), referenceReader.convert(new TypeReference<Map<Long, Integer>>(){}, singletonMap("123", "123")));
    }

}