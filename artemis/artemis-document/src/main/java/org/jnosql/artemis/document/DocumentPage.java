/*
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
 */
package org.jnosql.artemis.document;

import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An implementation of {@link Page} to Column.
 *
 * @param <T> the entity type
 */
final class DocumentPage<T> implements Page<T> {

    private final DocumentTemplate template;

    private final Stream<T> entities;

    private final DocumentQueryPagination query;

    private List<T> entitiesCache;

    DocumentPage(DocumentTemplate template, Stream<T> entities, DocumentQueryPagination query) {
        this.template = template;
        this.entities = entities;
        this.query = query;
    }

    @Override
    public Pagination getPagination() {
        return query.getPagination();
    }

    @Override
    public Page<T> next() {
        return template.select(query.next());
    }

    @Override
    public Stream<T> getContent() {
        return getEntities();
    }

    @Override
    public <C extends Collection<T>> C getContent(Supplier<C> collectionFactory) {
        Objects.requireNonNull(collectionFactory, "collectionFactory is required");
        return getEntities().collect(Collectors.toCollection(collectionFactory));
    }

    @Override
    public Stream<T> get() {
        return getEntities();
    }

    private Stream<T> getEntities() {
        if (Objects.isNull(entitiesCache)) {
            synchronized (this) {
                this.entitiesCache = entities.collect(Collectors.toList());
            }
        }
        return entitiesCache.stream();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DocumentPage<?> that = (DocumentPage<?>) o;
        return Objects.equals(entities, that.entities) &&
                Objects.equals(query, that.query);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities, query);
    }

    @Override
    public String toString() {
        return "DocumentPage{" +
                "entities=" + entities +
                ", query=" + query +
                '}';
    }
}
