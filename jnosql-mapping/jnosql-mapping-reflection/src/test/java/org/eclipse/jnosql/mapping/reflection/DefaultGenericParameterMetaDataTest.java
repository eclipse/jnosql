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
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.GenericParameterMetaData;
import org.eclipse.jnosql.mapping.test.entities.Book;
import org.eclipse.jnosql.mapping.test.entities.constructor.BookUser;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)

class DefaultGenericParameterMetaDataTest {

    @Inject
    private ReflectionClassConverter converter;

    private GenericParameterMetaData fieldMetadata;

    @BeforeEach
    public void setUp(){
        EntityMetadata entityMetadata = converter.create(BookUser.class);
        ConstructorMetadata constructor = entityMetadata.constructor();
        this.fieldMetadata = (GenericParameterMetaData)
                constructor.parameters().stream().filter(p -> p.name().equals("books"))
                .findFirst().orElseThrow();
    }
    @Test
    public void shouldToString() {
        assertThat(fieldMetadata.toString()).isNotEmpty().isNotNull();
    }

    @Test
    public void shouldGetElementType(){
        assertThat(fieldMetadata.elementType()).isEqualTo(Book.class);
    }

    @Test
    public void shouldCollectionInstance(){
        Collection<?> collection = this.fieldMetadata.collectionInstance();
        assertThat(collection).isInstanceOf(List.class);
    }
}