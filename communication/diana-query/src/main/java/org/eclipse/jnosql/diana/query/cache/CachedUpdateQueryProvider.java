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

import jakarta.nosql.query.UpdateQuery;
import jakarta.nosql.query.UpdateQuery.UpdateQueryProvider;
import org.eclipse.jnosql.diana.query.AntlrUpdateQueryProvider;

import java.util.Objects;

/**
 * The {@link AntlrUpdateQueryProvider} cache wrapper.
 */
public final class CachedUpdateQueryProvider implements UpdateQueryProvider {

    private final CacheQuery<UpdateQuery> cached;


    public CachedUpdateQueryProvider() {
        this.cached = CacheQuery.of(q -> new AntlrUpdateQueryProvider().apply(q));
    }

    @Override
    public UpdateQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
