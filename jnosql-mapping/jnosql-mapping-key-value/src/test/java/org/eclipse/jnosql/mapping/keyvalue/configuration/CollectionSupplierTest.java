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
package org.eclipse.jnosql.mapping.keyvalue.configuration;

import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.keyvalue.BucketManagerFactory;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.keyvalue.KeyValue;
import org.eclipse.jnosql.mapping.keyvalue.KeyValueEntityConverter;
import org.eclipse.jnosql.mapping.keyvalue.MockProducer;
import org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.KEY_VALUE_DATABASE;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.KEY_VALUE_PROVIDER;
import static org.junit.jupiter.api.Assertions.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, KeyValueEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, KeyValueExtension.class})
class CollectionSupplierTest {

    @Inject
    @KeyValue("names")
    private List<String> names;

    @BeforeAll
    public static void beforeEach(){
        System.clearProperty(KEY_VALUE_PROVIDER.get());
        System.clearProperty(KEY_VALUE_DATABASE.get());
    }

    @Test
    public void shouldGetList() {
        System.setProperty(KEY_VALUE_PROVIDER.get(), KeyValueConfigurationMock3.class.getName());
        Assertions.assertNotNull(names);
        Mockito.verify(KeyValueConfigurationMock3.FACTORY).getList(Mockito.eq("names"), Mockito.eq(String.class));
    }
}