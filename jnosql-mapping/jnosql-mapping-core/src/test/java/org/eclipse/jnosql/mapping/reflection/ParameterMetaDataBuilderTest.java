/*
 *  Copyright (c) 2022 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.mapping.AttributeConverter;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.MoneyConverter;
import jakarta.nosql.tck.entities.constructor.Computer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class ParameterMetaDataBuilderTest {


    //parameter wit collection
    //parameter with EMBEDDED
    //parameter with entity

    @Test
    public void shouldConvertIdParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData id = ParameterMetaDataBuilder.of(constructor.getParameters()[0]);
        Assertions.assertNotNull(id);
        Assertions.assertTrue(id.isId());
        Assertions.assertEquals("_id", id.getName());
        Assertions.assertEquals(Long.class, id.getType());
        Assertions.assertEquals(MappingType.DEFAULT, id.getParamType());
        Assertions.assertTrue(id.getConverter().isEmpty());
    }

    @Test
    public void shouldConvertDefaultParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData name = ParameterMetaDataBuilder.of(constructor.getParameters()[1]);
        Assertions.assertNotNull(name);
        Assertions.assertFalse(name.isId());
        Assertions.assertEquals("name", name.getName());
        Assertions.assertEquals(String.class, name.getType());
        Assertions.assertEquals(MappingType.DEFAULT, name.getParamType());
        Assertions.assertTrue(name.getConverter().isEmpty());
    }

    @Test
    public void shouldConvertConverterParameter() {
        Constructor<Computer> constructor = (Constructor<Computer>) Computer.class.getDeclaredConstructors()[0];
        ParameterMetaData price = ParameterMetaDataBuilder.of(constructor.getParameters()[4]);
        Assertions.assertNotNull(price);
        Assertions.assertFalse(price.isId());
        Assertions.assertEquals("price", price.getName());
        Assertions.assertEquals(Money.class, price.getType());
        Assertions.assertEquals(MappingType.DEFAULT, price.getParamType());
        Assertions.assertFalse(price.getConverter().isEmpty());
        Class<? extends AttributeConverter<Object, Object>> converter = price.getConverter().orElseThrow();
        assertEquals(MoneyConverter.class, converter);
    }


}