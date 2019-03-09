/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.diana.api.reader;

import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.TypeReferenceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DequeTypeReferenceReaderTest {

    private TypeReferenceReader referenceReader = new DequeTypeReferenceReader();

    @Test
    public void shouldBeCompatible() {

        assertTrue(referenceReader.isCompatible(new TypeReference<Deque<String>>() {
        }));
        assertTrue(referenceReader.isCompatible(new TypeReference<Deque<Long>>() {
        }));

    }


    @Test
    public void shouldNotBeCompatible() {
        assertFalse(referenceReader.isCompatible(new TypeReference<ArrayList<BigDecimal>>() {
        }));
        assertFalse(referenceReader.isCompatible(new TypeReference<String>() {
        }));
        assertFalse(referenceReader.isCompatible(new TypeReference<Set<String>>() {
        }));
        assertFalse(referenceReader.isCompatible(new TypeReference<List<List<String>>>() {
        }));
        assertFalse(referenceReader.isCompatible(new TypeReference<Map<Integer, String>>() {
        }));
    }


    @Test
    public void shouldConvert() {
        assertEquals(new ArrayDeque<>(singletonList("123")).getFirst(), referenceReader.convert(new TypeReference<Deque<String>>() {
        }, "123").getFirst());
        assertEquals(new ArrayDeque<>(singletonList(123L)).getFirst(), referenceReader.convert(new TypeReference<Deque<Long>>() {
        }, "123").getFirst());

    }

    @Test
    public void shouldConvertAndBeMutuable() {
        Deque<String> strings = referenceReader.convert(new TypeReference<Deque<String>>() {
        }, "123");
        strings.add("456");
        Assertions.assertEquals(2, strings.size());
    }

    @Test
    public void shouldConvertAndBeMutuable2() {
        Deque<String> strings = referenceReader.convert(new TypeReference<Deque<String>>() {
        }, Arrays.asList("123", "32"));
        strings.add("456");
        Assertions.assertEquals(3, strings.size());
    }

}