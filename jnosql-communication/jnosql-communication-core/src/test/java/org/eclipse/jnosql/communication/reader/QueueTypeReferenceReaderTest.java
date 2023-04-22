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
 *   Elias Nogueira
 *
 */
package org.eclipse.jnosql.communication.reader;

import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class QueueTypeReferenceReaderTest {

    private final TypeReferenceReader referenceReader = new QueueTypeReferenceReader();

    @DisplayName("Should be compatible")
    @ParameterizedTest(name = "compatible {0}")
    @MethodSource("compatibleTypeReferences")
    void shouldBeCompatible(TypeReference<?> type) {
        assertThat(referenceReader.test(type)).isTrue();
    }

    @DisplayName("Should be incompatible")
    @ParameterizedTest(name = "compatible {0}")
    @MethodSource("incompatibleTypeReferences")
    void shouldNotBeCompatible(TypeReference<?> type) {
        assertThat(referenceReader.test(type)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert")
    void shouldConvert() {
        assertSoftly(softly -> {
            softly.assertThat(referenceReader.convert(new TypeReference<Queue<String>>() {
            }, "123")).as("TypeReference<Queue<String>> conversion").isEqualTo(new LinkedList<>(singletonList("123")));

            softly.assertThat(referenceReader.convert(new TypeReference<Queue<Long>>() {
            }, "123")).as("TypeReference<Queue<String>> conversion").isEqualTo(new LinkedList<>(singletonList(123L)));
        });
    }

    @Test
    @DisplayName("Should be able to convert and mutable")
    void shouldConvertAndBeMutable() {
        Queue<String> strings = referenceReader.convert(new TypeReference<>() {
        }, "123");
        strings.add("456");

        assertThat(strings).hasSize(2).contains("123", "456");
    }

    @Test
    @DisplayName("Should be able to convert and mutable an Array value")
    void shouldConvertAndBeMutable2() {
        Queue<String> strings = referenceReader.convert(new TypeReference<>() {
        }, Arrays.asList("123", "32"));
        strings.add("456");

        assertThat(strings).hasSize(3).contains("123", "32", "456");
    }

    static Stream<Arguments> compatibleTypeReferences() {
        return Stream.of(
                arguments(new TypeReference<Queue<String>>() {
                }),
                arguments(new TypeReference<Queue<Long>>() {
                }),
                arguments(new TypeReference<Deque<String>>() {
                }),
                arguments(new TypeReference<Deque<Long>>() {
                })
        );
    }

    static Stream<Arguments> incompatibleTypeReferences() {
        return Stream.of(
                arguments(new TypeReference<ArrayList<BigDecimal>>() {
                }),
                arguments(new TypeReference<String>() {
                }),
                arguments(new TypeReference<Set<String>>() {
                }),
                arguments(new TypeReference<List<List<String>>>() {
                }),
                arguments(new TypeReference<Map<Integer, String>>() {
                })
        );
    }
}
