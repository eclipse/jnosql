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
import org.eclipse.jnosql.mapping.metadata.ConstructorBuilder;
import org.eclipse.jnosql.mapping.metadata.ConstructorMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReflectionConstructorBuilderSupplierTest {

    @Mock
    private ConstructorMetadata constructorMetadata;

    private ReflectionConstructorBuilderSupplier supplier;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        supplier = new ReflectionConstructorBuilderSupplier();
    }

    @Test
    public void shouldApply() {
        // Mocking ConstructorBuilder
        ConstructorBuilder constructorBuilder = DefaultConstructorBuilder.of(constructorMetadata);
        when(constructorBuilder.toString()).thenReturn("MockedConstructorBuilder");

        // Applying the supplier
        ConstructorBuilder result = supplier.apply(constructorMetadata);

        Assertions.assertThat(result).isNotNull();
    }
}