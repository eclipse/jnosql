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
package org.eclipse.jnosql.communication.query.cache;

import jakarta.nosql.query.DeleteQuery;
import jakarta.nosql.query.DeleteQuery.DeleteQueryProvider;
import org.eclipse.jnosql.communication.query.AntlrDeleteQueryProvider;

import java.util.Objects;

/**
 * The {@link AntlrDeleteQueryProvider} cache wrapper.
 */
public final class CachedDeleteQueryProvider implements DeleteQueryProvider {

    private final CacheQuery<DeleteQuery> cached;


    public CachedDeleteQueryProvider() {
        this.cached = CacheQuery.of(q -> new AntlrDeleteQueryProvider().apply(q));
    }

    @Override
    public DeleteQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
