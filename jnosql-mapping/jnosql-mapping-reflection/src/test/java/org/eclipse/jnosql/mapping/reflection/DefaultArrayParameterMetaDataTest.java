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

import org.eclipse.jnosql.mapping.metadata.ArrayParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ClassConverter;
import org.eclipse.jnosql.mapping.metadata.CollectionParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.entities.Book;
import org.eclipse.jnosql.mapping.reflection.entities.constructor.BookUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class DefaultArrayParameterMetaDataTest {

    private ArrayParameterMetaData fieldMetadata;

    @BeforeEach
    void setUp(){
        ClassConverter converter = new ReflectionClassConverter();
        EntityMetadata entityMetadata = converter.apply(BookUser.class);
        ConstructorMetadata constructor = entityMetadata.constructor();
        this.fieldMetadata = (ArrayParameterMetaData)
                constructor.parameters().stream().filter(p -> p.name().equals("magazines"))
                .findFirst().orElseThrow();
    }

    @Test
    void shouldToString() {
        assertThat(fieldMetadata.toString()).isNotEmpty().isNotNull();
    }

    @Test
    void shouldGetElementType(){
        assertThat(fieldMetadata.elementType()).isEqualTo(Book.class);
    }

    @Test
    void shouldArrayInstance() {
        List<String> phones = List.of("Ada", "Lovelace");
        String[] value = (String[]) fieldMetadata.arrayInstance(phones);
        assertThat(value).containsExactly("Ada", "Lovelace");
    }

}