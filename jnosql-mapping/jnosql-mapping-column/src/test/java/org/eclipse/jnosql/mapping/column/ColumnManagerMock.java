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
package org.eclipse.jnosql.mapping.column;

import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.communication.column.ColumnConfiguration;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.communication.column.ColumnManagerFactory;
import org.mockito.Mockito;

public class ColumnManagerMock implements ColumnConfiguration {



    @Override
    public MockFamilyManager apply(Settings settings) {
        return new MockFamilyManager(settings);
    }

    public static class MockFamilyManager implements ColumnManagerFactory {
        private final Settings settings;

        public MockFamilyManager(Settings settings) {
            this.settings = settings;
        }

        @Override
        public ColumnManager apply(String database) {
            return Mockito.mock(ColumnManager.class);
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public void close() {

        }
    }
}
