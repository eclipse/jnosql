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
package org.eclipse.jnosql.artemis.configuration;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.document.DocumentConfigurationAsync;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;

import java.time.Duration;
import java.util.function.Consumer;
import java.util.stream.Stream;

class DocumentConfigurationAsyncMock implements DocumentConfigurationAsync {

    @Override
    public DocumentCollectionManagerAsyncFactoryMock get() {
        return new DocumentCollectionManagerAsyncFactoryMock(Settings.builder().build());
    }

    @Override
    public DocumentCollectionManagerAsyncFactoryMock get(Settings settings) {
        return new DocumentCollectionManagerAsyncFactoryMock(settings);
    }

    public static class DocumentCollectionManagerAsyncFactoryMock implements DocumentCollectionManagerAsyncFactory {

        private final Settings settings;

        public DocumentCollectionManagerAsyncFactoryMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }


        @Override
        public DocumentCollectionManagerAsyncMock getAsync(String database) {
            return new DocumentCollectionManagerAsyncMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class DocumentCollectionManagerAsyncMock implements DocumentCollectionManagerAsync {

        private final String database;

        public DocumentCollectionManagerAsyncMock(String database) {
            this.database = database;
        }

        public String getDatabase() {
            return database;
        }

        @Override
        public void insert(DocumentEntity entity) {

        }

        @Override
        public void insert(DocumentEntity entity, Duration ttl) {

        }

        @Override
        public void insert(Iterable<DocumentEntity> entities) {

        }

        @Override
        public void insert(Iterable<DocumentEntity> entities, Duration ttl) {

        }

        @Override
        public void insert(DocumentEntity entity, Consumer<DocumentEntity> callBack) {

        }

        @Override
        public void insert(DocumentEntity entity, Duration ttl, Consumer<DocumentEntity> callBack) {

        }

        @Override
        public void update(DocumentEntity entity) {

        }

        @Override
        public void update(Iterable<DocumentEntity> entities) {

        }

        @Override
        public void update(DocumentEntity entity, Consumer<DocumentEntity> callBack) {

        }

        @Override
        public void delete(DocumentDeleteQuery query) {

        }

        @Override
        public void delete(DocumentDeleteQuery query, Consumer<Void> callBack) {

        }

        @Override
        public void select(DocumentQuery query, Consumer<Stream<DocumentEntity>> callBack) {

        }

        @Override
        public void count(String documentCollection, Consumer<Long> callback) {

        }

        @Override
        public void close() {

        }
    }
}
