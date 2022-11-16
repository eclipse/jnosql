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
package org.eclipse.jnosql.mapping.column.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnManager;
import jakarta.nosql.column.ColumnManagerFactory;
import jakarta.nosql.column.ColumnQuery;

import java.time.Duration;
import java.util.stream.Stream;

public class ColumnConfigurationMock2 implements ColumnConfiguration {


    @Override
    public ColumnManagerFactoryMock apply(Settings settings) {
        return new ColumnManagerFactoryMock(settings);
    }


    public static class ColumnManagerFactoryMock implements ColumnManagerFactory {

        private final Settings settings;

        public Settings getSettings() {
            return settings;
        }

        public ColumnManagerFactoryMock(Settings settings) {
            this.settings = settings;
        }

        @Override
        public ColumnManagerMock apply(String database) {
            return new ColumnManagerMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class ColumnManagerMock implements ColumnManager {

        private final String database;

        public ColumnManagerMock(String database) {
            this.database = database;
        }

        public String getDatabase() {
            return database;
        }

        @Override
        public String getName() {
            return database;
        }

        @Override
        public ColumnEntity insert(ColumnEntity entity) {
            return null;
        }

        @Override
        public ColumnEntity update(ColumnEntity entity) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> update(Iterable<ColumnEntity> entities) {
            return null;
        }

        @Override
        public ColumnEntity insert(ColumnEntity entity, Duration ttl) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities) {
            return null;
        }

        @Override
        public Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities, Duration ttl) {
            return null;
        }

        @Override
        public void delete(ColumnDeleteQuery query) {

        }

        @Override
        public Stream<ColumnEntity> select(ColumnQuery query) {
            return null;
        }

        @Override
        public long count(String columnFamily) {
            return 0;
        }

        @Override
        public void close() {

        }
    }
}
