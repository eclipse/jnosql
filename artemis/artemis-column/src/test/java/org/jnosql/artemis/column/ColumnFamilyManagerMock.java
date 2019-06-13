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

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import org.jnosql.diana.column.UnaryColumnConfiguration;
import org.mockito.Mockito;

public class ColumnFamilyManagerMock implements UnaryColumnConfiguration {


    @Override
    public ColumnFamilyManagerFactory get() {
        return null;
    }

    @Override
    public ColumnFamilyManagerFactory get(Settings settings) throws NullPointerException {
        return new MockFamilyManager(settings);
    }

    @Override
    public ColumnFamilyManagerAsyncFactory getAsync() {
        return null;
    }

    @Override
    public ColumnFamilyManagerAsyncFactory getAsync(Settings settings) throws NullPointerException {
        return new MockFamilyManager(settings);
    }



    public class MockFamilyManager implements ColumnFamilyManagerFactory, ColumnFamilyManagerAsyncFactory {
        private final Settings settings;

        public MockFamilyManager(Settings settings) {
            this.settings = settings;
        }

        @Override
        public ColumnFamilyManagerAsync getAsync(String database){
            return Mockito.mock(ColumnFamilyManagerAsync.class);
        }

        @Override
        public ColumnFamilyManager get(String database) {
            return Mockito.mock(ColumnFamilyManager.class);
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public void close() {

        }
    }
}
