package org.apache.diana.api.key;

import java.util.Map;

/**
 * The diana configuration to create a {@link KeyValueEntityManagerFactory}
 *
 * @author Otávio Santana
 */
public interface KeyValueConfiguration {

    /**
     * Reads configuration from Map and creates a {@link KeyValueEntityManagerFactory} instance.
     *
     * @param configurations
     * @return a {@link KeyValueEntityManagerFactory} instance
     */
    KeyValueEntityManagerFactory getManagerFactory(Map<String, String> configurations);

    /**
     * Reads configuration either from default configuration or a file defined by NoSQL provider and then creates a {@link KeyValueEntityManagerFactory} instance.
     *
     * @return
     */
    KeyValueEntityManagerFactory getManagerFactory();
}
