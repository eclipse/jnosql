/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import java.time.Duration;
import java.util.Optional;

/**
 * To either insert or overrides values from a key-value database use the <b>PUT</b> statement.
 */
public final class PutQuery implements Query {

    private final QueryValue<?> key;

    private final QueryValue<?> value;

    private final Duration ttl;

    PutQuery(QueryValue<?> key, QueryValue<?> value, Duration ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

    /**
     * The key
     * @return the key
     */
    public QueryValue<?> key() {
        return key;
    }

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     * @return the TTL otherwise {@link Optional#empty()}
     */
    public QueryValue<?> value() {
        return value;
    }

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     * @return the TTL otherwise {@link Optional#empty()}
     */
    public Optional<Duration> ttl() {
        return Optional.ofNullable(ttl);
    }


    @Override
    public String toString() {
        StringBuilder put = new StringBuilder();
        put.append("put {").append(key).append(" , ").append(value);
        if (ttl != null) {
            put.append(",").append(ttl);
        }
        put.append("}");
        return put.toString();
    }
}
