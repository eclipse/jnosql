package org.apache.diana.api.document;

import java.util.Map;

/**
 * The diana configuration to create a {@link DocumentCollectionManagerFactory}
 *
 * @author Otávio Santana
 */
public interface DocumentConfiguration {

    /**
     * Reads configuration from Map and creates a {@link DocumentCollectionManagerFactory} instance.
     *
     * @param configurations a configuration from {@link Map}
     * @return a {@link DocumentCollectionManagerFactory} instance
     */
    DocumentCollectionManagerFactory getManagerFactory(Map<String, String> configurations);

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider and then creates a {@link DocumentCollectionManagerFactory} instance.
     *
     * @return a {@link DocumentCollectionManagerFactory} instance
     */
    DocumentCollectionManagerFactory getManagerFactory();
}
