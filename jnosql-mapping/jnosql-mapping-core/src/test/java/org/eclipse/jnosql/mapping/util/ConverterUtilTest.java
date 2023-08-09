/*
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
 */
package org.eclipse.jnosql.mapping.util;

import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.test.entities.Money;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


@EnableAutoWeld
@AddPackages(value = Converters.class)
@AddPackages(value = VetedConverter.class)
@AddPackages(value = Reflections.class)
@AddExtensions(EntityMetadataExtension.class)
public class ConverterUtilTest {


    @Inject
    private Converters converters;

    @Inject
    private EntitiesMetadata mappings;

    @Test
    public void shouldNotConvert() {
        EntityMetadata mapping = mappings.get(Person.class);
        Object value = 10_000L;
        Object id = ConverterUtil.getValue(value, mapping, "id", converters);
        assertEquals(id, value);
    }

    @Test
    public void shouldConvert() {
        EntityMetadata mapping = mappings.get(Person.class);
        String value = "100";
        Object id = ConverterUtil.getValue(value, mapping, "id", converters);
        assertEquals(100L, id);
    }

    @Test
    public void shouldUseAttributeConvert() {
        EntityMetadata mapping = mappings.get(Worker.class);
        Object value = new Money("BRL", BigDecimal.TEN);
        Object converted = ConverterUtil.getValue(value, mapping, "salary", converters);
        assertEquals("BRL 10", converted);
    }
}