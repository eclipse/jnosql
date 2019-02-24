/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.column;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsyncFactory;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CDIExtension.class)
public class ColumnConfigurationProducerTest {


    @Inject
    @ConfigurationUnit(fileName = "column.json", name = "name")
    private ColumnFamilyManagerFactory<?> factoryA;

    @Inject
    @ConfigurationUnit(fileName = "column.json", name = "name-2")
    private ColumnFamilyManagerFactory factoryB;


    @Inject
    @ConfigurationUnit(fileName = "column.json", name = "name")
    private ColumnFamilyManagerAsyncFactory<?> factoryAsyncA;

    @Inject
    @ConfigurationUnit(fileName = "column.json", name = "name-2")
    private ColumnFamilyManagerAsyncFactory factoryAsyncB;


    @Test
    public void shouldReadInjectColumnFamily() {
        factoryA.get("database");
        assertTrue(factoryA instanceof ColumnFamilyManagerMock.MockFamilyManager);
        ColumnFamilyManagerMock.MockFamilyManager mock = (ColumnFamilyManagerMock.MockFamilyManager) factoryA;
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));

    }

    @Test
    public void shouldReadInjectColumnFamilyB() {
        factoryB.get("database");
        assertTrue(factoryB instanceof ColumnFamilyManagerMock.MockFamilyManager);
        ColumnFamilyManagerMock.MockFamilyManager mock = (ColumnFamilyManagerMock.MockFamilyManager) factoryB;
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
        assertEquals("value3", settings.get("key3"));

    }

    @Test
    public void shouldReadInjectColumnFamilyAsync() {
        factoryAsyncA.getAsync("database");
        assertTrue(factoryAsyncA instanceof ColumnFamilyManagerMock.MockFamilyManager);
        ColumnFamilyManagerMock.MockFamilyManager mock = (ColumnFamilyManagerMock.MockFamilyManager) factoryAsyncA;
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
    }

    @Test
    public void shouldReadInjectColumnFamilyBAsync() {
        factoryAsyncB.getAsync("database");
        assertTrue(factoryAsyncB instanceof ColumnFamilyManagerMock.MockFamilyManager);
        ColumnFamilyManagerMock.MockFamilyManager mock = (ColumnFamilyManagerMock.MockFamilyManager) factoryAsyncB;
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
        assertEquals("value3", settings.get("key3"));
    }

}