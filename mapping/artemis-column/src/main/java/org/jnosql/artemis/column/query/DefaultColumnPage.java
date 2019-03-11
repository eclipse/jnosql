/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.column.query;

import org.jnosql.artemis.Pagination;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

/**
 * Default implementation of {@link ColumnPage}
 *
 * @param <T> the entity type
 */
final class DefaultColumnPage<T> implements ColumnPage<T> {

    private final ColumnQuery query;

    private final ColumnTemplate template;

    private final Pagination pagination;

    private List<T> entities;

    DefaultColumnPage(ColumnQuery query, ColumnTemplate template, Pagination pagination) {
        this.query = query;
        this.template = template;
        this.pagination = pagination;
    }


    @Override
    public ColumnQuery getQuery() {
        return query;
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public ColumnPage<T> next() {
        Pagination nextPage = pagination.next();
        ColumnQuery nextQuery = new ArtemisColumnQuery(query.getSorts(),
                nextPage.getLimit(),
                nextPage.getSkip(),
                query.getCondition().orElse(null),
                query.getColumnFamily());

        return new DefaultColumnPage<>(nextQuery, template, nextPage);
    }

    @Override
    public List<T> getContent() {
        if (entities == null) {
            this.entities = template.select(query);
        }
        return entities;
    }

    @Override
    public <C extends Collection<T>> C getContent(Supplier<C> collectionFactory) {
        Objects.requireNonNull(collectionFactory, "collectionFactory is required");
        return get().collect(toCollection(collectionFactory));
    }

    @Override
    public Stream<T> get() {
        return getContent().stream();
    }

}
