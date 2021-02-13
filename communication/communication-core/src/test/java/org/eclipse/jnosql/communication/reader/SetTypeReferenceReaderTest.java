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
package org.eclipse.jnosql.communication.reader;

import jakarta.nosql.TypeReference;
import jakarta.nosql.TypeReferenceReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SetTypeReferenceReaderTest {

    private final TypeReferenceReader referenceReader = new SetTypeReferenceReader();

    @Test
    public void shouldBeCompatible() {

        assertTrue(referenceReader.test(new TypeReference<Set<String>>(){}));
        assertTrue(referenceReader.test(new TypeReference<Set<Long>>(){}));

    }


    @Test
    public void shouldNotBeCompatible() {
        assertFalse(referenceReader.test(new TypeReference<String>(){}));
        assertFalse(referenceReader.test(new TypeReference<List<String>>(){}));
        assertFalse(referenceReader.test(new TypeReference<List<List<String>>>(){}));
        assertFalse(referenceReader.test(new TypeReference<Queue<String>>(){}));
        assertFalse(referenceReader.test(new TypeReference<Map<Integer, String>>(){}));
    }


    @Test
    public void shouldConvert() {
        assertEquals(singleton("123"), referenceReader.convert(new TypeReference<List<String>>(){}, "123"));
        assertEquals(singleton(123L), referenceReader.convert(new TypeReference<List<Long>>(){}, "123"));
    }

    @Test
    public void shouldConvertAndBeMutable() {
        Set<String> strings = referenceReader.convert(new TypeReference<Set<String>>() {
        }, "123");
        strings.add("456");
        Assertions.assertEquals(2, strings.size());
    }

    @Test
    public void shouldConvertAndBeMutable2() {
        Set<String> strings = referenceReader.convert(new TypeReference<Set<String>>() {
        }, Arrays.asList("123", "32"));
        strings.add("456");
        Assertions.assertEquals(3, strings.size());
    }
}