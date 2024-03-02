/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.semistructured;

import jakarta.data.Sort;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery.DeleteQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultDeleteQueryBuilder implements DeleteQueryBuilder {

    private final List<String> documents = new ArrayList<>();

    private final List<Sort> sorts = new ArrayList<>();

    private String documentCollection;

    private CriteriaCondition condition;


    @Override
    public DeleteQueryBuilder delete(String column) {
        Objects.requireNonNull(column, "column is required");
        this.documents.add(column);
        return this;
    }

    @Override
    public DeleteQueryBuilder delete(String... columns) {
        Consumer<String> validNull = c -> requireNonNull(c, "there is null column in the query");
        Consumer<String> consume = this.documents::add;
        Stream.of(columns).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public DeleteQueryBuilder from(String documentCollection) {
        Objects.requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }

    @Override
    public DeleteQueryBuilder where(CriteriaCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public DeleteQuery build() {
        if (Objects.isNull(documentCollection)) {
            throw new IllegalArgumentException("The document collection is mandatory to build");
        }
        return new DefaultDeleteQuery(documentCollection, condition, documents);
    }

    @Override
    public void delete(DatabaseManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        manager.delete(build());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultDeleteQueryBuilder that = (DefaultDeleteQueryBuilder) o;
        return Objects.equals(documents, that.documents)
                && Objects.equals(sorts, that.sorts)
                && Objects.equals(documentCollection, that.documentCollection)
                && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documents, sorts, documentCollection, condition);
    }

    @Override
    public String toString() {
        return "DefaultDeleteQueryBuilder{" +
                "documents=" + documents +
                ", sorts=" + sorts +
                ", documentCollection='" + documentCollection + '\'' +
                ", condition=" + condition +
                '}';
    }
}
