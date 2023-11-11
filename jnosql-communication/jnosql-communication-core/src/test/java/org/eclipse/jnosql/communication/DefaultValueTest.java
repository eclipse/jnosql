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
 *   Maximillian Arruda
 *   Elias Nogueira
 *
 */
package org.eclipse.jnosql.communication;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonMap;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DefaultValueTest {

    private static final Value VALUE_OF_DOUBLE = Value.of(10L);

    @Test
    @DisplayName("Should return NullPointerException when element is null")
    void shouldReturnErrorWhenElementIsNull() {
        assertThatNullPointerException().isThrownBy(() -> Value.of(null)).withMessage("value is required");
    }

    @Test
    @DisplayName("Should be instance of the defined object")
    void shouldIsInstanceOf() {
        Value value = Value.of("12");

        assertThat(value.get()).isInstanceOf(String.class).isNotInstanceOf(Integer.class);
    }

    @Test
    @DisplayName("Should be able to convert the defined object type")
    void shouldConvertType() {
        Value value = Value.of(new AtomicInteger(5_000));

        assertSoftly(softly -> {
            softly.assertThat(value.get(Integer.class)).as("convert to Integer")
                    .isEqualTo(Integer.valueOf(5_000)).isInstanceOf(Integer.class);

            softly.assertThat(value.get(String.class))
                    .as("convert to String").isEqualTo("5000").isInstanceOf(String.class);
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to a singleton List")
    void shouldConvertToSingletonList() {
        assertSoftly(softly -> {
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<List<String>>() {
            })).as("TypeSupplier List of String").containsExactly("10");
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<List<Long>>() {
            })).as("TypeSupplier List of Long").containsExactly(10L);
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to a Stream")
    void shouldConvertToStream() {
        assertSoftly(softly -> {
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<Stream<String>>() {
            }).collect(toList())).as("TypeSupplier Stream of String").containsExactly("10");
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<Stream<Long>>() {
            }).collect(toList())).as("TypeSupplier Stream of Long").containsExactly(10L);
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to List")
    void shouldConvertToList() {
        Value value = Value.of(asList(10, 20, 30));

        assertSoftly(softly -> {
            softly.assertThat(value.get(new TypeReference<List<String>>() {
            })).as("TypeSupplier List of String").containsExactly("10", "20", "30");
            softly.assertThat(value.get(new TypeReference<List<BigInteger>>() {
                    })).as("TypeSupplier List of BigInteger")
                    .containsExactly(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to a singleton Set")
    void shouldConvertToSingletonSet() {
        assertSoftly(softly -> {
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<Set<String>>() {
            })).as("TypeSupplier Singleton Stream of String").containsExactly("10");
            softly.assertThat(VALUE_OF_DOUBLE.get(new TypeReference<List<Long>>() {
            })).as("TypeSupplier Singleton Stream of Long").containsExactly(10L);
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to a Set")
    void shouldConvertToSet() {
        Value value = Value.of(asList(10, 20, 30));

        assertSoftly(softly -> {
            softly.assertThat(value.get(new TypeReference<Set<String>>() {
            })).as("Should convert as Set<String>").contains("10", "20", "30");
            softly.assertThat(value.get(new TypeReference<List<BigInteger>>() {
                    })).as("Should convert List<BigInteger>")
                    .containsExactly(BigInteger.TEN, BigInteger.valueOf(20L), BigInteger.valueOf(30L));
        });
    }

    @Test
    @DisplayName("Should be able to convert a defined object to a Map")
    void shouldConvertMap() {
        Map<String, Integer> map = singletonMap("ONE", 1);
        Value value = Value.of(map);

        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result).containsKey("ONE").containsValue(1);
    }

    @Test
    @DisplayName("Should be able to convert the map key and value")
    void shouldConvertKeyValueInsideMap() {
        Map<Integer, String> map = singletonMap(10, "1");
        Value value = Value.of(map);

        Map<String, Integer> result = value.get(new TypeReference<>() {
        });
        assertThat(result).containsKey("10").containsValue(1);
    }

    @Test
    @DisplayName("Should throw UnsupportedOperationException when key has a different object type")
    @SuppressWarnings("unused")
    void shouldConvertMapIgnoringKeyValue() {
        assertThatThrownBy(() -> {
            Map<Integer, List<String>> map = singletonMap(10, asList("1", "2", "3"));
            Value value = Value.of(map);

            Map<String, List<String>> result = value.get(new TypeReference<>() {
            });
        }).isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("The type TypeReference{type=java.util.Map<java.lang.String, java.util.List<java.lang.String>>} is not supported");
    }

    @Test
    void shouldHasCode(){
        var value = Value.of(asList(10, 20, 30));
        var valueB = Value.of(asList(10, 20, 30));
        Assertions.assertThat(value.hashCode()).isEqualTo(valueB.hashCode());
    }

    @Test
    void shouldEquals(){
        var value = Value.of(asList(10, 20, 30));
        var valueB = Value.of(asList(10, 20, 30));
        Assertions.assertThat(value).isEqualTo(valueB);
    }
}
