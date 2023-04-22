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

import org.eclipse.jnosql.communication.ValueReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class EnumReaderTest {

    private final ValueReader valueReader = new EnumReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.test(Enum.class)).as("Enum is compatible").isTrue();
            softly.assertThat(valueReader.test(ExampleNumber.class)).as("ExampleNumber is compatible").isTrue();
        });
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(valueReader.test(AtomicBoolean.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the value to ExampleNumber")
    void shouldConvert() {
        ExampleNumber exampleNumberOne = ExampleNumber.ONE;
        ExampleNumber exampleNumberTwo = ExampleNumber.TWO;

        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(ExampleNumber.class, exampleNumberOne)).as("ExampleNumber conversion")
                    .isEqualTo(exampleNumberOne);

            softly.assertThat(valueReader.read(ExampleNumber.class, 0)).as("Integer conversion").isEqualTo(exampleNumberOne);
            softly.assertThat(valueReader.read(ExampleNumber.class, "ONE")).as("String conversion").isEqualTo(exampleNumberOne);
            softly.assertThat(valueReader.read(ExampleNumber.class, 1)).as("Integer conversion").isEqualTo(exampleNumberTwo);
            softly.assertThat(valueReader.read(ExampleNumber.class, "TWO")).as("String conversion").isEqualTo(exampleNumberTwo);
        });
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when class is not assignable")
    void shouldThrowIllegalArgumentExceptionWhenClassIsNotAssignable() {
        assertThatIllegalArgumentException().isThrownBy(() -> valueReader.read(List.class, "ONE"))
                .withMessage("The informed class isn't an enum type: interface java.util.List");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when there is no index")
    void shouldReturnErrorInIndex() {
        assertThatIllegalArgumentException().isThrownBy(() -> valueReader.read(ExampleNumber.class, 10))
                .withMessage("There is not index in enum to value: 10");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when there is no value")
    void shouldReturnErrorInName() {
        assertThatIllegalArgumentException().isThrownBy(() -> valueReader.read(ExampleNumber.class, "FOUR"))
                .withMessage("There isn't name in enum to value: FOUR");
    }

    enum ExampleNumber {
        ONE, TWO
    }
}
