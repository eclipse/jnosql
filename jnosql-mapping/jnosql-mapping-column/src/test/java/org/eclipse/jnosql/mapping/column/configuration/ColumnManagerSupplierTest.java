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
package org.eclipse.jnosql.mapping.column.configuration;

import jakarta.nosql.column.ColumnManager;
import jakarta.data.exceptions.MappingException;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.COLUMN_DATABASE;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.COLUMN_PROVIDER;

@CDIExtension
class ColumnManagerSupplierTest {

    @Inject
    private ColumnManagerSupplier supplier;

    @BeforeEach
    public void beforeEach(){
        System.clearProperty(COLUMN_PROVIDER.get());
        System.clearProperty(COLUMN_DATABASE.get());
    }

    @Test
    public void shouldGetManager() {
        System.setProperty(COLUMN_PROVIDER.get(), ColumnConfigurationMock.class.getName());
        System.setProperty(COLUMN_DATABASE.get(), "database");
        ColumnManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(ColumnConfigurationMock.ColumnManagerMock.class);
    }


    @Test
    public void shouldUseDefaultConfigurationWhenProviderIsWrong() {
        System.setProperty(COLUMN_PROVIDER.get(), Integer.class.getName());
        System.setProperty(COLUMN_DATABASE.get(), "database");
        ColumnManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(ColumnConfigurationMock2.ColumnManagerMock.class);
    }

    @Test
    public void shouldUseDefaultConfiguration() {
        System.setProperty(COLUMN_DATABASE.get(), "database");
        ColumnManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(ColumnConfigurationMock2.ColumnManagerMock.class);
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotDatabase() {
        Assertions.assertThrows(MappingException.class, () -> supplier.get());
    }
}