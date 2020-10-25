/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.column.reactive.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import jakarta.nosql.column.ColumnQuery;

import java.time.Duration;
import java.util.stream.Stream;

class ColumnConfigurationMock implements ColumnConfiguration {

    @Override
    public ColumnFamilyFactoryMock get() {
        return new ColumnFamilyFactoryMock(Settings.builder().build());
    }

    @Override
    public ColumnFamilyFactoryMock get(Settings settings) {
        return new ColumnFamilyFactoryMock(settings);
    }

    public static class ColumnFamilyFactoryMock implements ColumnFamilyManagerFactory {

        private final Settings settings;

        public ColumnFamilyFactoryMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public ColumnFamilyManagerMock  get(String database) {
            return new ColumnFamilyManagerMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class ColumnFamilyManagerMock implements ColumnFamilyManager {

        private final String database;

        public ColumnFamilyManagerMock(String database) {
            this.database = database;
        }

        public String getDatabase() {
            return database;
        }


        @Override
        public ColumnEntity insert(ColumnEntity columnEntity) {
            return null;
        }

        @Override
        public ColumnEntity update(ColumnEntity columnEntity) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> update(Iterable<ColumnEntity> iterable) {
            return null;
        }

        @Override
        public ColumnEntity insert(ColumnEntity columnEntity, Duration duration) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> insert(Iterable<ColumnEntity> iterable) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> insert(Iterable<ColumnEntity> iterable, Duration duration) {
            return null;
        }

        @Override
        public void delete(ColumnDeleteQuery columnDeleteQuery) {

        }

        @Override
        public Stream<ColumnEntity> select(ColumnQuery columnQuery) {
            return null;
        }

        @Override
        public long count(String s) {
            return 0;
        }

        @Override
        public void close() {

        }
    }
}