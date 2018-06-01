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
import org.jnosql.diana.api.key.KeyValuePreparedStatement;
import org.jnosql.query.GetQuery;
import org.jnosql.query.GetQuerySupplier;
import org.jnosql.query.QueryException;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

final class GetQueryParser {

    private final GetQuerySupplier supplier;

    GetQueryParser() {
        this.supplier = GetQuerySupplier.getSupplier();
    }

    Iterable<Value> query(String query, BucketManager manager) {

        GetQuery getQuery = supplier.apply(query);
        Params params = new Params();
        List<Value> values = getQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        if (params.isNotEmpty()) {
            throw new QueryException("To run a query with a parameter use a PrepareStatement instead.");
        }

        List<Object> keys = values.stream().map(Value::get).collect(toList());
        return manager.get(keys);
    }

    public KeyValuePreparedStatement prepare(String query, BucketManager manager) {
        GetQuery getQuery = supplier.apply(query);
        Params params = new Params();
        List<Value> values = getQuery.getKeys().stream().map(k -> Values.getValue(k, params)).collect(toList());
        return DefaultKeyValuePreparedStatement.get(values, manager, params, query);
    }
}
