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
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.metadata.GenericFieldMetadata;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DefaultGenericFieldMetaDataTest {

    private GenericFieldMetadata fieldMetadata;

    @BeforeEach
    public void setUp(){
        ClassConverter converter = new ReflectionClassConverter();
        EntityMetadata entityMetadata = converter.apply(Person.class);
        FieldMetadata phones = entityMetadata.fieldMapping("phones").orElseThrow();
        this.fieldMetadata = (GenericFieldMetadata) phones;
    }
    @Test
    public void shouldToString() {
        assertThat(fieldMetadata.toString()).isNotEmpty().isNotNull();
    }

    @Test
    public void shouldGetElementType(){
        assertThat(fieldMetadata.elementType()).isEqualTo(String.class);
    }

    @Test
    public void shouldCollectionInstance(){
        Collection<?> collection = this.fieldMetadata.collectionInstance();
        assertThat(collection).isInstanceOf(List.class);
    }

    @Test
    public void shouldEqualsHashCode(){
        Assertions.assertThat(fieldMetadata).isEqualTo(fieldMetadata);
        Assertions.assertThat(fieldMetadata).hasSameHashCodeAs(fieldMetadata);
    }

    @Test
    public void shouldValue(){
        List<String> phones = List.of("Ada", "Lovelace");
        Object value = fieldMetadata.value(Value.of(phones));
        assertThat(value).isNotNull().isInstanceOf(List.class);
    }

    @Test
    public void shouldConverter(){
        assertThat(fieldMetadata.converter()).isNotNull().isEmpty();
    }

    @Test
    public void shouldNewConverter(){
        assertThat(fieldMetadata.newConverter()).isNotNull().isEmpty();
    }
}
