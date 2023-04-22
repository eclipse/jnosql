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

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class BooleanReaderTest {

    private final ValueReader valueReader = new BooleanReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.test(AtomicBoolean.class)).as("AtomicBoolean is compatible").isTrue();
            softly.assertThat(valueReader.test(Boolean.class)).as("Boolean is compatible").isTrue();
        });
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(valueReader.test(Number.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the AtomicBoolean for 'true' value returns")
    void shouldConvertAtomicBooleanExpectingTrue() {
        AtomicBoolean atomicBooleanTrue = new AtomicBoolean(true);

        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(AtomicBoolean.class, atomicBooleanTrue)).as("AtomicBoolean conversion as true")
                    .isEqualTo(atomicBooleanTrue);

            softly.assertThat(valueReader.read(AtomicBoolean.class, 10).get()).as("Integer conversion as true")
                    .isEqualTo(atomicBooleanTrue.get());

            softly.assertThat(valueReader.read(AtomicBoolean.class, -1).get()).as("Integer with negative value conversion as true")
                    .isEqualTo(atomicBooleanTrue.get());

            softly.assertThat(valueReader.read(AtomicBoolean.class, "true").get()).as("String with 'true' value conversion as true")
                    .isEqualTo(atomicBooleanTrue.get());

            softly.assertThat(valueReader.read(AtomicBoolean.class, true).get()).as("boolean with 'true' value conversion as true")
                    .isEqualTo(atomicBooleanTrue.get());
        });
    }

    @Test
    @DisplayName("Should be able to convert the AtomicBoolean for 'false' value returns")
    void shouldConvertAtomicBooleanExpectingFalse() {
        AtomicBoolean atomicBooleanFalse = new AtomicBoolean(false);

        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(AtomicBoolean.class, 0).get()).as("Integer conversion as false")
                    .isEqualTo(atomicBooleanFalse.get());

            softly.assertThat(valueReader.read(AtomicBoolean.class, "false").get()).as("String with negative value conversion as false")
                    .isEqualTo(atomicBooleanFalse.get());

            softly.assertThat(valueReader.read(AtomicBoolean.class, false).get()).as("boolean with 'true' value conversion as false")
                    .isEqualTo(atomicBooleanFalse.get());

        });
    }

    @Test
    @DisplayName("Should be able to convert the value to the Boolean object for 'true' returns")
    void shouldConvertBooleanExpectingTrue() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(Boolean.class, new AtomicBoolean(true))).as("AtomicBoolean conversion as true").isEqualTo(Boolean.TRUE);
            softly.assertThat(valueReader.read(Boolean.class, 10)).as("Integer conversion as true").isEqualTo(Boolean.TRUE);
            softly.assertThat(valueReader.read(Boolean.class, -1)).as("Integer with negative value conversion as true").isEqualTo(Boolean.TRUE);
            softly.assertThat(valueReader.read(Boolean.class, "true")).as("String with 'true' value conversion as true").isEqualTo(Boolean.TRUE);
            softly.assertThat(valueReader.read(Boolean.class, true)).as("boolean with 'true' value conversion as true").isEqualTo(Boolean.TRUE);

            softly.assertThat(valueReader.read(Boolean.class, 0)).as("Integer conversion is false").isEqualTo(Boolean.FALSE);
            softly.assertThat(valueReader.read(Boolean.class, "false")).as("String with 'false' value conversion as false").isEqualTo(Boolean.FALSE);
            softly.assertThat(valueReader.read(Boolean.class, false)).as("boolean with 'false' value conversion as false").isEqualTo(Boolean.FALSE);
        });
    }

    @Test
    @DisplayName("Should be able to convert the value to the Boolean object for 'false' returns")
    void shouldConvertBooleanExpectingFalse() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(Boolean.class, 0)).as("Integer conversion as false").isEqualTo(Boolean.FALSE);
            softly.assertThat(valueReader.read(Boolean.class, "false")).as("String conversion as false").isEqualTo(Boolean.FALSE);
            softly.assertThat(valueReader.read(Boolean.class, false)).as("boolean conversion as false").isEqualTo(Boolean.FALSE);
        });
    }
}
