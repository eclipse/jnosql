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
package org.jnosql.artemis.document;

import org.jnosql.artemis.CDIExtension;
import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CDIExtension.class)
public class DocumentConfigurationProducerXMLTest {

    @Inject
    @ConfigurationUnit(fileName = "document.xml", name = "name")
    private DocumentCollectionManagerFactory<?> factoryA;

    @Inject
    @ConfigurationUnit(fileName = "document.xml", name = "name-2")
    private DocumentCollectionManagerFactory factoryB;


    @Inject
    @ConfigurationUnit(fileName = "document.xml", name = "name")
    private DocumentCollectionManagerAsyncFactory<?> factoryAsyncA;

    @Inject
    @ConfigurationUnit(fileName = "document.xml", name = "name-2")
    private DocumentCollectionManagerAsyncFactory factoryAsyncB;


    @Test
    public void shouldReadInjectDocumentCollection() {
        factoryA.get("database");
        assertTrue(factoryA instanceof DocumentCollectionManagerMock.DocumentMock);
        DocumentCollectionManagerMock.DocumentMock mock = (DocumentCollectionManagerMock.DocumentMock) factoryA;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
    }

    @Test
    public void shouldReadInjectDocumentCollectionB() {
        factoryB.get("database");
        assertTrue(factoryB instanceof DocumentCollectionManagerMock.DocumentMock);
        DocumentCollectionManagerMock.DocumentMock mock = (DocumentCollectionManagerMock.DocumentMock) factoryB;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
        assertEquals("value3", settings.get("key3").get());
    }

    @Test
    public void shouldReadInjectDocumentCollectionAsync() {
        factoryAsyncA.getAsync("database");
        assertTrue(factoryAsyncA instanceof DocumentCollectionManagerMock.DocumentMock);
        DocumentCollectionManagerMock.DocumentMock mock = (DocumentCollectionManagerMock.DocumentMock) factoryAsyncA;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
    }

    @Test
    public void shouldReadInjectDocumentCollectionAsyncB() {
        factoryAsyncB.getAsync("database");
        assertTrue(factoryAsyncB instanceof DocumentCollectionManagerMock.DocumentMock);
        DocumentCollectionManagerMock.DocumentMock mock = (DocumentCollectionManagerMock.DocumentMock) factoryAsyncB;
        Settings settings = mock.getSettings();
        assertEquals("value", settings.get("key").get());
        assertEquals("value2", settings.get("key2").get());
        assertEquals("value3", settings.get("key3").get());
    }
}