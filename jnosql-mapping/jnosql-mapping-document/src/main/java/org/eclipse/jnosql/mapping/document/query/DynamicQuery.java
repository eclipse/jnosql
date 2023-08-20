/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.document.query;

import jakarta.data.repository.Limit;
import jakarta.data.repository.Sort;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.NoSQLPage;
import org.eclipse.jnosql.mapping.document.MappingDocumentQuery;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.repository.SpecialParameters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A query converter to update dynamic query to a {@link DocumentQuery}
 */
public class DynamicQuery implements Supplier<DocumentQuery> {

    private final SpecialParameters special;
    private final DocumentQuery query;

    private DynamicQuery(SpecialParameters special, DocumentQuery query) {
        this.special = special;
        this.query = query;
    }

    @Override
    public DocumentQuery get() {
        if (special.isEmpty()) {
            return query;
        }
        Optional<Limit> limit = special.limit();

        if (special.hasOnlySort()) {
            List<Sort> sorts = new ArrayList<>();
            sorts.addAll(query.sorts());
            sorts.addAll(special.sorts());
            long skip = limit.map(l -> l.startAt() - 1).orElse(query.skip());
            long max = limit.map(Limit::maxResults).orElse((int) query.limit());
            return new MappingDocumentQuery(sorts, max,
                    skip,
                    query.condition().orElse(null),
                    query.name());
        }

        if (limit.isPresent()) {
            long skip = limit.map(l -> l.startAt() - 1).orElse(query.skip());
            long max = limit.map(Limit::maxResults).orElse((int) query.limit());
            return new MappingDocumentQuery(query.sorts(), max,
                    skip,
                    query.condition().orElse(null),
                    query.name());
        }

        return special.pageable().<DocumentQuery>map(p -> {
            long size = p.size();
            long skip = NoSQLPage.skip(p);
            List<Sort> sorts = query.sorts();
            if (!special.sorts().isEmpty()) {
                sorts = new ArrayList<>(query.sorts());
                sorts.addAll(special.sorts());
            }
            return new MappingDocumentQuery(sorts, size, skip,
                    query.condition().orElse(null), query.name());
        }).orElse(query);
    }

    /**
     * Creates a {@link DynamicQuery} instance
     * @param args the method parameters
     * @param query the document query
     * @return the {@link DynamicQuery} instance
     * @throws NullPointerException when either args or query are null
     */
    public static DynamicQuery of(Object[] args, DocumentQuery query) {
        Objects.requireNonNull(args, "args is required");
        Objects.requireNonNull(query, "query is required");
        return new DynamicQuery(DynamicReturn.findSpecialParameters(args), query);
    }
}
