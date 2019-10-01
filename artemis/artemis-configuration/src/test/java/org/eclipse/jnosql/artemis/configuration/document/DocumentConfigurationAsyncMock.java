package org.eclipse.jnosql.artemis.configuration.document;

import jakarta.nosql.Settings;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCollectionManagerAsyncFactory;
import jakarta.nosql.document.DocumentCollectionManagerFactory;
import jakarta.nosql.document.DocumentConfiguration;
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
        public DocumentCollectionManagerMock getAsync(String database) {
            return new DocumentCollectionManagerMock(database);
        }

        @Override
        public void close() {

        }
    }

    public static class DocumentCollectionManagerMock implements DocumentCollectionManagerAsync {

        private final String database;

        public DocumentCollectionManagerMock(String database) {
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
