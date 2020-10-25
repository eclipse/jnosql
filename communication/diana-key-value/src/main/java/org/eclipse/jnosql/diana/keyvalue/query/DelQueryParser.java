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
package org.eclipse.jnosql.diana.keyvalue.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValuePreparedStatement;
import jakarta.nosql.query.DelQuery;
import jakarta.nosql.query.DelQuery.DelQueryProvider;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

final class DelQueryParser {

    private final DelQueryProvider provider;

    DelQueryParser() {
        this.provider = ServiceLoaderProvider.get(DelQueryProvider.class);
    }

    Stream<Value> query(String query, BucketManager manager) {

        DelQuery delQuery = provider.apply(query);
        Params params = Params.newParams();
        List<Value> values = delQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }

        List<Object> keys = values.stream().map(Value::get).collect(toList());
        manager.delete(keys);
        return Stream.empty();
    }

    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        DelQuery delQuery = provider.apply(query);
        Params params = Params.newParams();
        List<Value> values = delQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        return DefaultKeyValuePreparedStatement.del(values, manager, params, query);
    }
}
