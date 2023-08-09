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

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.test.entities.MoneyConverter;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class DefaultFieldMetadataTest {

    private ClassConverter converter;

    private DefaultFieldMetadata fieldMetadata;

    @BeforeEach
    public void setUp(){
        this.converter = new ReflectionClassConverter();
        EntityMetadata entityMetadata = converter.apply(Person.class);
        FieldMetadata name = entityMetadata.fieldMapping("name").orElseThrow();
        this.fieldMetadata = (DefaultFieldMetadata) name;
    }
    @Test
    public void shouldToString() {
        assertThat(fieldMetadata.toString()).isNotEmpty().isNotNull();
    }

    @Test
    public void shouldEqualsHashCode() {
        assertThat(fieldMetadata).isEqualTo(fieldMetadata);
        assertThat(fieldMetadata.hashCode()).isEqualTo(fieldMetadata.hashCode());
    }

    @Test
    public void shouldCreateNewInstanceConverter(){
        EntityMetadata entityMetadata = converter.apply(Worker.class);
        FieldMetadata name = entityMetadata.fieldMapping("salary").orElseThrow();
        fieldMetadata = (DefaultFieldMetadata) name;
        AttributeConverter<Object, Object> result = fieldMetadata.newConverter().orElseThrow();
        assertThat(result).isNotNull().isInstanceOf(MoneyConverter.class);
    }
}