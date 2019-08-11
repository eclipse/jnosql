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
package org.jnosql.diana.kv.query;


import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.Value;
import jakarta.nosql.kv.BucketManager;
import jakarta.nosql.kv.KeyValuePreparedStatement;
import jakarta.nosql.query.GetQuery;
import jakarta.nosql.query.GetQuery.GetQueryProvider;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

final class GetQueryParser {

    private final GetQueryProvider provider;

    GetQueryParser() {
        this.provider = ServiceLoaderProvider.get(GetQueryProvider.class);
    }

    List<Value> query(String query, BucketManager manager) {

        GetQuery getQuery = provider.apply(query);
        Params params = Params.newParams();
        List<Value> values = getQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }

        List<Object> keys = values.stream().map(Value::get).collect(toList());
        List<Value> result = new ArrayList<>();
        manager.get(keys).forEach(result::add);
        return result;
    }

    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        GetQuery getQuery = provider.apply(query);
        Params params = Params.newParams();
        List<Value> values = getQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        return DefaultKeyValuePreparedStatement.get(values, manager, params, query);
    }
}
