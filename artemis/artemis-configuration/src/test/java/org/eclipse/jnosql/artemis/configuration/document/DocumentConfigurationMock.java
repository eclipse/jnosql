package org.eclipse.jnosql.artemis.configuration.document;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;

import java.time.Duration;
import java.util.stream.Stream;

class DocumentConfigurationMock implements DocumentConfiguration {

    @Override
    public DocumentCollectionManagerFactoryMock get() {
        return new DocumentCollectionManagerFactoryMock(Settings.builder().build());
    }

    @Override
    public DocumentCollectionManagerFactoryMock get(Settings settings) {
        return new DocumentCollectionManagerFactoryMock(settings);
    }

    public static class DocumentCollectionManagerFactoryMock implements DocumentCollectionManagerFactory {

        private final Settings settings;

        public DocumentCollectionManagerFactoryMock(Settings settings) {
            this.settings = settings;
        }

        public Settings getSettings() {
            return settings;
        }

        @Override
        public DocumentCollectionManagerMock  get(String database) {
            return new DocumentCollectionManagerMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class DocumentCollectionManagerMock implements DocumentCollectionManager {

        private final String database;

        public DocumentCollectionManagerMock(String database) {
            this.database = database;
        }

        public String getDatabase() {
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
