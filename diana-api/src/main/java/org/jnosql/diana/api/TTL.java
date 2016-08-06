package org.jnosql.diana.api;

import java.io.Serializable;

/**
 * Entity that defines time to live an entity
 *
 * @author Otávio Santana
 */
public interface TTL extends Serializable {

    /**
     * Returns TTL converted in nanoseconds
     *
     * @return TTL in nanoseconds
     */
    long toNanos();

    /**
     * Returns TTL converted in microseconds
     *
     * @return TTL in microseconds
     */
    long toMicros();

    /**
     * Returns TTL converted in milliseconds
     *
     * @return TTL in milliseconds
     */
    long toMillis();

    /**
     * Returns TTL converted in seconds
     *
     * @return TTL in seconds
     */
    long toSeconds();

    /**
     * Returns TTL converted in minutes
     *
     * @return TTL in minutes
     */
    long toMinutes();

    /**
     * Returns TTL converted in days
     *
     * @return TTL in days
     */
    long toDays();

}
