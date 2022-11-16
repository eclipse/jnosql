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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import org.mockito.Mockito;

public class DocumentManagerMock implements DocumentConfiguration {


    @Override
    public DocumentMock apply(Settings settings) throws NullPointerException {
        return new DocumentMock(settings);
    }

    public static class DocumentMock implements DocumentManagerFactory {

        private final Settings settings;

        DocumentMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public DocumentManager apply(String database) {
            return Mockito.mock(DocumentManager.class);
        }

        @Override
        public void close() {

        }
    }
}
