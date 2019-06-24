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
package org.jnosql.diana.reader;

import jakarta.nosql.TypeReference;
import jakarta.nosql.TypeReferenceReader;;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static java.util.Collections.singletonMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @Test
    public void shouldSholdcreateMutuableMap() {
        Map<String, String> map = referenceReader.convert(new TypeReference<Map<String, String>>() {
        }, singletonMap(123, 123L));

        map.put("23", "123");

        assertEquals(2, map.size());
    }


    @Test
    public void shouldSholdcreateMutuableMap2() {
        Map<Integer, Long> oldMap = new HashMap<>();
        oldMap.put(1, 234L);
        oldMap.put(2, 2345L);
        Map<String, String> map = referenceReader.convert(new TypeReference<Map<String, String>>() {
        }, oldMap);

        map.put("23", "123");

        assertEquals(3, map.size());
    }
}