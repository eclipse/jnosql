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
package org.eclipse.jnosql.mapping;

import jakarta.data.repository.Page;
import jakarta.data.repository.Pageable;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * A JNoSQL implementation of {@link  Page}
 *
 * @param <T> the entity type
 */
public class NoSQLPage<T> implements Page<T> {

    private final List<T> entities;

    private final Pageable pageable;

    private NoSQLPage(List<T> entities, Pageable pageable) {
        this.entities = entities;
        this.pageable = pageable;
    }

    @Override
    public long totalElements() {
        throw new UnsupportedOperationException("JNoSQL has no support for this feature yet");
    }

    @Override
    public long totalPages() {
        throw new UnsupportedOperationException("JNoSQL has no support for this feature yet");
    }

    @Override
    public List<T> content() {
        return Collections.unmodifiableList(entities);
    }

    @Override
    public boolean hasContent() {
        return !this.entities.isEmpty();
    }

    @Override
    public int numberOfElements() {
        return this.entities.size();
    }

    @Override
    public Pageable pageable() {
        return this.pageable;
    }

    @Override
    public Pageable nextPageable() {
        return this.pageable.next();
    }

    @Override
    public Iterator<T> iterator() {
        return this.entities.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NoSQLPage<?> noSQLPage = (NoSQLPage<?>) o;
        return Objects.equals(entities, noSQLPage.entities) && Objects.equals(pageable, noSQLPage.pageable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entities, pageable);
    }

    @Override
    public String toString() {
        return "NoSQLPage{" +
                "entities=" + entities +
                ", pageable=" + pageable +
                '}';
    }

    /**
     * Creates a {@link  Page} implementation from entities and a pageable
     * @param entities the entities
     * @param pageable the pageable
     * @return a {@link Page} instance
     * @param <T> the entity type
     */
    public static <T> Page<T> of(List<T> entities, Pageable pageable) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(pageable, "pageable is required");
        return new NoSQLPage<>(entities, pageable);
    }
}
