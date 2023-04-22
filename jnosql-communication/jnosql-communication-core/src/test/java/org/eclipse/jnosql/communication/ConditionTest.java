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
package org.eclipse.jnosql.communication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.eclipse.jnosql.communication.Condition.AND;
import static org.eclipse.jnosql.communication.Condition.EQUALS;
import static org.eclipse.jnosql.communication.Condition.GREATER_EQUALS_THAN;
import static org.eclipse.jnosql.communication.Condition.IN;
import static org.eclipse.jnosql.communication.Condition.LESSER_THAN;
import static org.eclipse.jnosql.communication.Condition.NOT;
import static org.eclipse.jnosql.communication.Condition.OR;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ConditionTest {

    @DisplayName("Should return the value of given Condition constant")
    @ParameterizedTest(name = "Condition {0} has the same as field value {1}")
    @MethodSource("data")
    void shouldReturnNameField(Condition condition, String fieldName) {
        assertThat(condition.getNameField()).isEqualTo(fieldName);
    }

    @DisplayName("Should parse Condition enum constant from value")
    @ParameterizedTest(name = "parsed field Condition {0} has the same value as {1}")
    @MethodSource("data")
    void shouldParser(Condition condition, String value) {
        assertThat(Condition.parse(value)).isEqualTo(condition);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when condition is not found")
    void shouldThrowIllegalArgumentException() {
        String nonExistentCondition = "_NON_EXISTENT";
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Condition.parse(nonExistentCondition))
                .withMessage(String.format("The condition %s is not found", nonExistentCondition));
    }

    static Stream<Arguments> data() {
        return Stream.of(
                arguments(AND, "_AND"),
                arguments(EQUALS, "_EQUALS"),
                arguments(GREATER_EQUALS_THAN, "_GREATER_EQUALS_THAN"),
                arguments(IN, "_IN"),
                arguments(NOT, "_NOT"),
                arguments(OR, "_OR"),
                arguments(LESSER_THAN, "_LESSER_THAN")
        );
    }
}
