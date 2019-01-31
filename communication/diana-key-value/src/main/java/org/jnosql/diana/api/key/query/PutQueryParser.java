/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana.api.key.query;

import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.key.BucketManager;
import org.jnosql.diana.api.key.KeyValueEntity;
import org.jnosql.diana.api.key.KeyValuePreparedStatement;
import org.jnosql.query.PutQuery;
import org.jnosql.query.PutQuerySupplier;
import org.jnosql.query.QueryException;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class PutQueryParser {

    private final PutQuerySupplier supplier;

    PutQueryParser() {
        this.supplier = PutQuerySupplier.getSupplier();
    }

    List<Value> query(String query, BucketManager manager) {

        PutQuery putQuery = supplier.apply(query);
        Params params = new Params();
        Value key = Values.getValue(putQuery.getKey(), params);
        Value value = Values.getValue(putQuery.getValue(), params);
        Optional<Duration> ttl = putQuery.getTtl();

        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }

        KeyValueEntity<Object> entity = KeyValueEntity.of(key.get(), value.get());
        if (ttl.isPresent()) {
            manager.put(entity, ttl.get());
        } else {
            manager.put(entity);
        }
        return Collections.emptyList();
    }

    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        PutQuery putQuery = supplier.apply(query);
        Params params = new Params();
        Value key = Values.getValue(putQuery.getKey(), params);
        Value value = Values.getValue(putQuery.getValue(), params);
        Optional<Duration> ttl = putQuery.getTtl();

        return DefaultKeyValuePreparedStatement.put(key, value, manager, params, ttl.orElse(null), query);
    }
}
