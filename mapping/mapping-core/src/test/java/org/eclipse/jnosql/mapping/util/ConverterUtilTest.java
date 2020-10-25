/*
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
 */
package org.eclipse.jnosql.mapping.util;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@CDIExtension
public class ConverterUtilTest {


    @Inject
    private Converters converters;

    @Inject
    private ClassMappings mappings;

    @Test
    public void shouldNotConvert() {
        ClassMapping mapping = mappings.get(Person.class);
        Object value = 10_000L;
        Object id = ConverterUtil.getValue(value, mapping, "id", converters);
        assertEquals(id, value);
    }

    @Test
    public void shouldConvert() {
        ClassMapping mapping = mappings.get(Person.class);
        String value = "100";
        Object id = ConverterUtil.getValue(value, mapping, "id", converters);
        assertEquals(100L, id);
    }

    @Test
    public void shouldUseAttributeConvert() {
        ClassMapping mapping = mappings.get(Worker.class);
        Object value = new Money("BRL", BigDecimal.TEN);
        Object converted = ConverterUtil.getValue(value, mapping, "salary", converters);
        assertEquals("BRL 10", converted);
    }
}