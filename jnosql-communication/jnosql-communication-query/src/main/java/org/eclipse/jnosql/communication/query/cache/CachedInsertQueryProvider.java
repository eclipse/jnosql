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
package org.eclipse.jnosql.communication.query.cache;

import jakarta.nosql.query.InsertQuery;
import jakarta.nosql.query.InsertQuery.InsertQueryProvider;
import org.eclipse.jnosql.communication.query.AntlrInsertQueryProvider;

import java.util.Objects;

/**
 * The {@link AntlrInsertQueryProvider} cache wrapper.
 */
public final class CachedInsertQueryProvider implements InsertQueryProvider {

    private final CacheQuery<InsertQuery> cached;

    public CachedInsertQueryProvider() {
        this.cached = CacheQuery.of(q -> new AntlrInsertQueryProvider().apply(q));
    }

    @Override
    public InsertQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
