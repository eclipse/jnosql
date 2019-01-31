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

import org.jnosql.aphrodite.antlr.AntlrGetQuerySupplier;
import org.jnosql.query.GetQuery;
import org.jnosql.query.GetQuerySupplier;

import java.util.Objects;


/**
 * The {@link AntlrGetQuerySupplier} cache wrapper.
 */
public final class CachedGetQuerySupplier implements GetQuerySupplier {

    private final CacheQuery<GetQuery> cached;


    public CachedGetQuerySupplier() {
        this.cached = CacheQuery.of(q -> new AntlrGetQuerySupplier().apply(q));
    }

    @Override
    public GetQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
