package org.jnosql.diana.api.document;

/**
 * The diana configuration to create a {@link DocumentCollectionManagerFactory}
 *
 * @param <ASYNC> the type {@link DocumentCollectionManagerAsyncFactory}
 */
public interface DocumentConfigurationAsync<ASYNC extends DocumentCollectionManagerAsyncFactory> {

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider and then creates a
     * {@link DocumentCollectionManagerAsyncFactory} instance.
     *
     * @return a {@link DocumentCollectionManagerAsyncFactory} instance
     * @throws UnsupportedOperationException when the operation is not supported
     */
    ASYNC getAsync() throws UnsupportedOperationException;
}
