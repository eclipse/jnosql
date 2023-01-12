/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StreamTypeReferenceReaderTest {

    private final TypeReferenceReader referenceReader = new StreamTypeReferenceReader();

    @Test
    public void shouldBeCompatible() {

        assertTrue(referenceReader.test(new TypeReference<Stream<String>>(){}));
        assertTrue(referenceReader.test(new TypeReference<Stream<Long>>(){}));

    }


    @Test
    public void shouldNotBeCompatible() {
        assertFalse(referenceReader.test(new TypeReference<ArrayList<BigDecimal>>(){}));
        assertFalse(referenceReader.test(new TypeReference<String>(){}));
        assertFalse(referenceReader.test(new TypeReference<Set<String>>(){}));
        assertFalse(referenceReader.test(new TypeReference<List<List<String>>>(){}));
        assertFalse(referenceReader.test(new TypeReference<Queue<String>>(){}));
        assertFalse(referenceReader.test(new TypeReference<Map<Integer, String>>(){}));
    }


    @Test
    public void shouldConvert() {
        Stream<String> stream = referenceReader.convert(new TypeReference<>() {
        }, "123");
        assertEquals("123", stream.findAny().get());
    }
}