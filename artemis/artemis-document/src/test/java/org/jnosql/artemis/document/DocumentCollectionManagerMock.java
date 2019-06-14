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

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import org.jnosql.diana.document.UnaryDocumentConfiguration;
import org.mockito.Mockito;

public class DocumentCollectionManagerMock implements UnaryDocumentConfiguration {

    @Override
    public DocumentCollectionManagerFactory get() {
        return null;
    }

    @Override
    public DocumentCollectionManagerFactory get(Settings settings) throws NullPointerException {
        return new DocumentMock(settings);
    }

    @Override
    public DocumentCollectionManagerAsyncFactory getAsync() {
        return null;
    }

    @Override
    public DocumentCollectionManagerAsyncFactory getAsync(Settings settings) throws NullPointerException {
        return new DocumentMock(settings);
    }

    public class DocumentMock implements DocumentCollectionManagerFactory, DocumentCollectionManagerAsyncFactory {

        private final Settings settings;

        DocumentMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public DocumentCollectionManagerAsync getAsync(String database) {
            return Mockito.mock(DocumentCollectionManagerAsync.class);
        }

        @Override
        public DocumentCollectionManager get(String database) {
            return Mockito.mock(DocumentCollectionManager.class);
        }

        @Override
        public void close() {

        }
    }
}
