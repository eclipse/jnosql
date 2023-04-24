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
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.Arguments.of;

class NavigableSetTypeReferenceReaderTest {

    private final static TypeReferenceReader referenceReader = new NavigableSetTypeReferenceReader();

    @DisplayName("Should be compatible")
    @ParameterizedTest(name = "compatible {0}")
    @MethodSource("compatibleTypeReferences")
    void shouldBeCompatible(TypeReference<?> type) {
        assertThat(referenceReader.test(type)).isTrue();
    }

    @DisplayName("Should be incompatible")
    @ParameterizedTest(name = "compatible {0}")
    @MethodSource("incompatibleTypeReferences")
    void shouldBeIncompatible(TypeReference<?> type) {
        assertThat(referenceReader.test(type)).isFalse();
    }

    @DisplayName("Should be able to convert")
    @ParameterizedTest(name = "convert {0} into {1}")
    @MethodSource("conversionData")
    void shouldConvert(List<?> list, TypeReference<?> typeSupplier, String value) {
        assertThat(referenceReader.convert(typeSupplier, value)).isEqualTo(new TreeSet<>(list));
    }

    @Test
    @DisplayName("Should convert and be mutable")
    void shouldConvertAndBeMutable() {
        NavigableSet<String> strings = referenceReader.convert(new TypeReference<>() {
        }, "123");
        strings.add("456");

        assertThat(strings).hasSize(2).contains("123", "456");
    }

    @Test
    @DisplayName("Should convert and be mutable using Arrays")
    void shouldConvertAndBeMutable2() {
        NavigableSet<String> strings = referenceReader.convert(new TypeReference<>() {
        }, Arrays.asList("123", "32"));
        strings.add("456");

        assertThat(strings).hasSize(3).contains("123", "32", "456");
    }

    static Stream<Arguments> compatibleTypeReferences() {
        return Stream.of(
                arguments(new TypeReference<SortedSet<String>>() {
                }),
                arguments(new TypeReference<SortedSet<Long>>() {
                }),
                arguments(new TypeReference<NavigableSet<Long>>() {
                }),
                arguments(new TypeReference<NavigableSet<Long>>() {
                })
        );
    }

    static Stream<Arguments> incompatibleTypeReferences() {
        return Stream.of(
                arguments(new TypeReference<SortedSet<Animal>>() {
                }),
                arguments(new TypeReference<NavigableSet<Animal>>() {
                }),
                arguments(new TypeReference<ArrayList<BigDecimal>>() {
                }),
                arguments(new TypeReference<String>() {
                }),
                arguments(new TypeReference<Set<String>>() {
                }),
                arguments(new TypeReference<List<List<String>>>() {
                }),
                arguments(new TypeReference<Queue<String>>() {
                }),
                arguments(new TypeReference<Map<Integer, String>>() {
                })
        );
    }

    static Stream<Arguments> conversionData() {
        return Stream.of(
                of(singletonList("123"), new TypeReference<SortedSet<String>>() {
                }, "123"),
                of(singletonList(123L), new TypeReference<SortedSet<Long>>() {
                }, "123"),
                of(singletonList("123"), new TypeReference<NavigableSet<String>>() {
                }, "123"),
                of(singletonList(123L), new TypeReference<NavigableSet<Long>>() {
                }, "123")
        );
    }

    private static class Animal {
    }
}
