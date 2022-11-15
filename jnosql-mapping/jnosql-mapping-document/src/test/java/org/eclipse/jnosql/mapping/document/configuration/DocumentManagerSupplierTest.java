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
package org.eclipse.jnosql.mapping.document.configuration;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.MappingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.KEY_VALUE_DATABASE;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.KEY_VALUE_PROVIDER;

class DocumentManagerSupplierTest {


    @Inject
    private DocumentManagerSupplier supplier;

    @BeforeEach
    public void beforeEach(){
        System.clearProperty(KEY_VALUE_PROVIDER.get());
        System.clearProperty(KEY_VALUE_DATABASE.get());
    }

    @Test
    public void shouldGetBucketManager() {
        System.setProperty(KEY_VALUE_PROVIDER.get(), DocumentConfigurationMock.class.getName());
        System.setProperty(KEY_VALUE_DATABASE.get(), "database");
        DocumentCollectionManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(DocumentConfigurationMock.DocumentCollectionManagerMock.class);
    }


    @Test
    public void shouldUseDefaultConfigurationWhenProviderIsWrong() {
        System.setProperty(KEY_VALUE_PROVIDER.get(), Integer.class.getName());
        System.setProperty(KEY_VALUE_DATABASE.get(), "database");
        DocumentCollectionManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(DocumentConfigurationMock2.DocumentCollectionManagerMock.class);
    }

    @Test
    public void shouldUseDefaultConfiguration() {
        System.setProperty(KEY_VALUE_DATABASE.get(), "database");
        DocumentCollectionManager manager = supplier.get();
        Assertions.assertNotNull(manager);
        assertThat(manager).isInstanceOf(DocumentConfigurationMock2.DocumentCollectionManagerMock.class);
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotDatabase() {
        Assertions.assertThrows(MappingException.class, () -> supplier.get());
    }
}