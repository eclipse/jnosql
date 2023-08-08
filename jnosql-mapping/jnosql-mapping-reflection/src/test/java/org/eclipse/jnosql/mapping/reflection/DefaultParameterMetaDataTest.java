/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.MappingType;
import org.eclipse.jnosql.mapping.metadata.ParameterMetaData;
import org.eclipse.jnosql.mapping.test.entities.constructor.BookUser;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)

class DefaultParameterMetaDataTest {

    @Inject
    private ClassConverter converter;

    private ParameterMetaData parameterMetaData;


    @BeforeEach
    public void setUp(){
        EntityMetadata entityMetadata = converter.create(BookUser.class);
        ConstructorMetadata constructor = entityMetadata.constructor();
        this.parameterMetaData =
                constructor.parameters().stream().filter(p -> p.name().equals("native_name"))
                        .findFirst().orElseThrow();
    }


    @Test
    public void shouldParamType() {
        assertEquals(MappingType.DEFAULT, parameterMetaData.mappingType());
    }

    @Test
    public void shouldName() {
        assertEquals("native_name", parameterMetaData.name());
    }

    @Test
    public void shouldType() {
        assertEquals(String.class, parameterMetaData.type());
    }

    @Test
    public void shouldIsId() {
        assertFalse(parameterMetaData.isId());
    }

    @Test
    public void shouldConverter() {
        Optional<Class<AttributeConverter<Object, Object>>> converter = parameterMetaData.converter();
        assertFalse(converter.isPresent());
    }

    @Test
    public void testNewConverter() {
        Assertions.assertThat(parameterMetaData.newConverter()).isEmpty();
    }
}