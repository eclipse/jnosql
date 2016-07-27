package org.apache.diana.api;

/**
 * A wrapper to {@link AutoCloseable}
 * @author Otávio Santana
 */
public interface CloseResource extends AutoCloseable {


    /**
     * closes a resource
     */
    void close();
}
