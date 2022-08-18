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

import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;
@CDIExtension
class ConstructorMetadataBuilderTest {

    @Inject
    private Reflections reflections;


    private ConstructorMetadataBuilder builder;

    @BeforeEach
    public void setUp() {
        this.builder = new ConstructorMetadataBuilder(reflections);
    }

    @Test
    public void shouldReturnEmptyMetadata() {
        ConstructorMetadata metadata = builder.build(Person.class);
        Assertions.assertNotNull(metadata);
        Assertions.assertTrue(metadata.getParameters().isEmpty());
    }
}