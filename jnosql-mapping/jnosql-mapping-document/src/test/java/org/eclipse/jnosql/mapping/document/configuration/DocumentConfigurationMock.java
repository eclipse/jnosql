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
package org.eclipse.jnosql.mapping.document.configuration;

import jakarta.nosql.Settings;
import org.eclipse.jnosql.communication.document.DocumentManager;
import jakarta.nosql.document.DocumentManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.communication.document.DocumentQuery;

import java.time.Duration;
import java.util.stream.Stream;

class DocumentConfigurationMock implements DocumentConfiguration {


    @Override
    public DocumentManagerFactoryMock apply(Settings settings) {
        return new DocumentManagerFactoryMock(settings);
    }

    public static class DocumentManagerFactoryMock implements DocumentManagerFactory {

        private final Settings settings;

        public DocumentManagerFactoryMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public DocumentManagerMock  apply(String database) {
            return new DocumentManagerMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class DocumentManagerMock implements DocumentManager {

        private final String database;

        public DocumentManagerMock(String database) {
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
        public DocumentEntity insert(DocumentEntity entity) {
            return null;
        }

        @Override
        public DocumentEntity insert(DocumentEntity entity, Duration ttl) {
            return null;
        }

        @Override
        public Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities) {
            return null;
        }

        @Override
        public Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities, Duration ttl) {
            return null;
        }

        @Override
        public DocumentEntity update(DocumentEntity entity) {
            return null;
        }

        @Override
        public Iterable<DocumentEntity> update(Iterable<DocumentEntity> entities) {
            return null;
        }

        @Override
        public void delete(DocumentDeleteQuery query) {

        }

        @Override
        public Stream<DocumentEntity> select(DocumentQuery query) {
            return null;
        }

        @Override
        public long count(String documentCollection) {
            return 0;
        }

        @Override
        public void close() {

        }
    }
}
