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
package org.jnosql.aphrodite.antlr.cache;

import org.jnosql.aphrodite.antlr.AntlrSelectQuerySupplier;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;

import java.util.Objects;

/**
 * The {@link AntlrSelectQuerySupplier} cache wrapper.
 */
public final class CachedSelectQuerySupplier implements SelectQuerySupplier {

    private final CacheQuery<SelectQuery> cached;


    public CachedSelectQuerySupplier() {
        this.cached = CacheQuery.of(q -> new AntlrSelectQuerySupplier().apply(q));
    }

    @Override
    public SelectQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
