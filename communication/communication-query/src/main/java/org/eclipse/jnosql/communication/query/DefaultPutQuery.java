/*
 *  Copyright (c) 2018 Otávio Santana and others
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

import jakarta.nosql.query.PutQuery;
import jakarta.nosql.query.QueryValue;

import java.time.Duration;
import java.util.Optional;

final class DefaultPutQuery implements PutQuery {

    private final QueryValue<?> key;

    private final QueryValue<?> value;

    private final Duration ttl;

    DefaultPutQuery(QueryValue<?> key, QueryValue<?> value, Duration ttl) {
        this.key = key;
        this.value = value;
        this.ttl = ttl;
    }

    @Override
    public QueryValue<?> getKey() {
        return key;
    }

    @Override
    public QueryValue<?> getValue() {
        return value;
    }

    @Override
    public Optional<Duration> getTtl() {
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
