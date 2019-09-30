/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.configuration.column;

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnConfigurationAsync;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.column.ColumnFamilyManagerFactory;

class ColumnConfigurationAsyncMock implements ColumnConfigurationAsync {

    @Override
    public ColumnFamilyManagerAsyncFactoryMock get() {
        return new ColumnFamilyManagerAsyncFactoryMock(Settings.builder().build());
    }

    @Override
    public ColumnFamilyManagerAsyncFactoryMock get(Settings settings) {
        return new ColumnFamilyManagerAsyncFactoryMock(settings);
    }


    public static class ColumnFamilyManagerAsyncFactoryMock implements ColumnFamilyManagerAsyncFactory {

        private final Settings settings;

        public Settings getSettings() {
            return settings;
        }

        public ColumnFamilyManagerAsyncFactoryMock(Settings settings) {
            this.settings = settings;
        }

        @Override
        public <T extends ColumnFamilyManagerAsync> T getAsync(String database) {
            return null;
        }

        @Override
        public void close() {

        }
    }
}
