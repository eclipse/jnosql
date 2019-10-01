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
        public <T extends DocumentCollectionManagerAsync> T getAsync(String database) {
            return null;
        }

        @Override
        public void close() {

        }
    }

    public static class DocumentCollectionManagerMock implements DocumentCollectionManager {

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
