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
package org.eclipse.jnosql.diana.query.cache;

import jakarta.nosql.query.RemoveQuery;
import jakarta.nosql.query.RemoveQuery.RemoveQueryProvider;
import org.eclipse.jnosql.diana.query.AntlrRemoveQueryProvider;

import java.util.Objects;

/**
 * The {@link AntlrRemoveQueryProvider} cache wrapper.
 */
public final class CachedRemoveQueryProvider implements RemoveQueryProvider {

    private final CacheQuery<RemoveQuery> cached;


    public CachedRemoveQueryProvider() {
        this.cached = CacheQuery.of(q -> new AntlrRemoveQueryProvider().apply(q));
    }

    @Override
    public RemoveQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
