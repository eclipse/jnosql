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

import jakarta.nosql.query.PutQuery;
import jakarta.nosql.query.PutQuery.PutQueryProvider;
import org.eclipse.jnosql.diana.query.AntlrPutQueryProvider;

import java.util.Objects;

/**
 * The {@link AntlrPutQueryProvider} cache wrapper.
 */
public final class CachedPutQueryProvider implements PutQueryProvider {

    private final CacheQuery<PutQuery> cached;


    public CachedPutQueryProvider() {
        this.cached = CacheQuery.of(q -> new AntlrPutQueryProvider().apply(q));
    }

    @Override
    public PutQuery apply(String query) {
        Objects.requireNonNull(query, "query is required");
        return cached.get(query);
    }
}
