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
package org.eclipse.jnosql.artemis.column.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnConfigurationAsync;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.column.ColumnQuery;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
        public ColumnFamilyManagerAsyncMock getAsync(String database) {
            return new ColumnFamilyManagerAsyncMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class ColumnFamilyManagerAsyncMock implements ColumnFamilyManagerAsync {


        private final String database;

        public ColumnFamilyManagerAsyncMock(String database) {
            this.database = database;
        }

        public String getDatabase() {
            return database;
        }

        @Override
        public void insert(ColumnEntity entity) {

        }

        @Override
        public void insert(ColumnEntity entity, Duration ttl) {

        }

        @Override
        public void insert(ColumnEntity entity, Consumer<ColumnEntity> callBack) {

        }

        @Override
        public void insert(Iterable<ColumnEntity> entities) {

        }

        @Override
        public void insert(Iterable<ColumnEntity> entities, Duration ttl) {

        }

        @Override
        public void insert(ColumnEntity entity, Duration ttl, Consumer<ColumnEntity> callBack) {

        }

        @Override
        public void update(ColumnEntity entity) {

        }

        @Override
        public void update(Iterable<ColumnEntity> entities) {

        }

        @Override
        public void update(ColumnEntity entity, Consumer<ColumnEntity> callBack) {

        }

        @Override
        public void delete(ColumnDeleteQuery query) {

        }

        @Override
        public void delete(ColumnDeleteQuery query, Consumer<Void> callBack) {

        }

        @Override
        public void select(ColumnQuery query, Consumer<Stream<ColumnEntity>> callBack) {

        }

        @Override
        public void count(String columnFamily, Consumer<Long> callback) {

        }

        @Override
        public void close() {

        }
    }
}
