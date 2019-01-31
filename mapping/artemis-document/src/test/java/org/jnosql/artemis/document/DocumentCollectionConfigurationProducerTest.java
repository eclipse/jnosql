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
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsyncFactory;
import org.jnosql.diana.api.document.DocumentCollectionManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(CDIExtension.class)
public class DocumentCollectionConfigurationProducerTest {

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name")
    private DocumentCollectionManagerFactory<?> factoryA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2")
    private DocumentCollectionManagerFactory factoryB;


    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name")
    private DocumentCollectionManagerAsyncFactory<?> factoryAsyncA;

    @Inject
    @ConfigurationUnit(fileName = "document.json", name = "name-2")
    private DocumentCollectionManagerAsyncFactory factoryAsyncB;


    @Test
    public void shouldReadInjectDocumentCollection() {
        factoryA.get("database");
        assertTrue(DocumentCollectionManagerMock.DocumentMock.class.isInstance(factoryA));
        DocumentCollectionManagerMock.DocumentMock mock = DocumentCollectionManagerMock.DocumentMock.class.cast(factoryA);
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
    }

    @Test
    public void shouldReadInjectDocumentCollectionB() {
        factoryB.get("database");
        assertTrue(DocumentCollectionManagerMock.DocumentMock.class.isInstance(factoryB));
        DocumentCollectionManagerMock.DocumentMock mock = DocumentCollectionManagerMock.DocumentMock.class.cast(factoryB);
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
        assertEquals("value3", settings.get("key3"));
    }

    @Test
    public void shouldReadInjectDocumentCollectionAsync() {
        factoryAsyncA.getAsync("database");
        assertTrue(DocumentCollectionManagerMock.DocumentMock.class.isInstance(factoryAsyncA));
        DocumentCollectionManagerMock.DocumentMock mock = DocumentCollectionManagerMock.DocumentMock.class.cast(factoryAsyncA);
        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
    }

    @Test
    public void shouldReadInjectDocumentCollectionAsyncB() {
        factoryAsyncB.getAsync("database");
        assertTrue(DocumentCollectionManagerMock.DocumentMock.class.isInstance(factoryAsyncB));
        DocumentCollectionManagerMock.DocumentMock mock = DocumentCollectionManagerMock.DocumentMock.class.cast(factoryAsyncB);

        Map<String, Object> settings = mock.getSettings();
        assertEquals("value", settings.get("key"));
        assertEquals("value2", settings.get("key2"));
        assertEquals("value3", settings.get("key3"));
    }
}