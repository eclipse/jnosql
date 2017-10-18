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

package org.jnosql.diana.api.writer;

import org.jnosql.diana.api.ValueWriter;
import org.junit.Before;
import org.junit.Test;

import java.time.temporal.Temporal;
import java.util.Optional;

import static org.junit.Assert.*;

public class ValueWriterDecoratorTest {

    private ValueWriter valueWriter;

    @Before
    public void setUp() {
        valueWriter = ValueWriterDecorator.getInstance();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(valueWriter.isCompatible(Optional.class));
        assertTrue(valueWriter.isCompatible(Temporal.class));
        assertFalse(valueWriter.isCompatible(Boolean.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldConvert() {
        String diana = "diana";
        Optional<String> optional = Optional.of(diana);
        Object result = valueWriter.write(optional);
        assertEquals(diana, result);
    }
}