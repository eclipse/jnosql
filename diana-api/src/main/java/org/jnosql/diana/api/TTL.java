/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.diana.api;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * Entity that defines time to live an entity
 */
public interface TTL extends Serializable {

    /**
     * Returns TTL in nanoseconds
     *
     * @param duration the duration
     * @return a TTL instance in nanoseconds
     */
    static TTL ofNanos(long duration) {
        return new DefaultTTL(duration, TimeUnit.NANOSECONDS);
    }

    /**
     * Returns TTL in microseconds
     *
     * @param duration the duration
     * @return a TTL instance in microseconds
     */
    static TTL ofMicros(long duration) {
        return new DefaultTTL(duration, TimeUnit.MICROSECONDS);
    }

    /**
     * Returns TTL in milliseconds
     *
     * @param duration the duration
     * @return a TTL instance in milliseconds
     */
    static TTL ofMillis(long duration) {
        return new DefaultTTL(duration, TimeUnit.MILLISECONDS);
    }

    /**
     * Returns TTL in seconds
     *
     * @param duration the duration
     * @return a TTL instance in seconds
     */
    static TTL ofSeconds(long duration) {
        return new DefaultTTL(duration, TimeUnit.SECONDS);
    }

    /**
     * Returns TTL in minutes
     *
     * @param duration the duration
     * @return a TTL instance in minutes
     */
    static TTL ofMinutes(long duration) {
        return new DefaultTTL(duration, TimeUnit.MINUTES);
    }

    /**
     * Returns TTL in hours
     *
     * @param duration the duration
     * @return a TTL instance in hours
     */
    static TTL ofHours(long duration) {
        return new DefaultTTL(duration, TimeUnit.HOURS);
    }

    /**
     * Returns TTL in days
     *
     * @param duration the duration
     * @return a TTL instance in days
     */
    static TTL ofDays(long duration) {
        return new DefaultTTL(duration, TimeUnit.DAYS);
    }


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
