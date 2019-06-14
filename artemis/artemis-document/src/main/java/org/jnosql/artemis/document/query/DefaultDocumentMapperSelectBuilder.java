/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.document.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import org.jnosql.artemis.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.Sort;
import jakarta.nosql.document.DocumentQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

class DefaultDocumentMapperSelectBuilder extends AbstractMapperQuery implements DocumentMapperFrom, DocumentMapperLimit,
        DocumentMapperSkip, DocumentMapperOrder, DocumentMapperNameCondition,
        DocumentMapperNotCondition, DocumentMapperNameOrder, DocumentMapperWhere {

    private final List<Sort> sorts = new ArrayList<>();


    DefaultDocumentMapperSelectBuilder(ClassMapping mapping, Converters converters) {
        super(mapping, converters);
    }


    @Override
    public DocumentMapperNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentMapperNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }

    @Override
    public DocumentMapperNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public DocumentMapperSkip skip(long start) {
        this.start = start;
        return this;
    }

    @Override
    public DocumentMapperLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public DocumentMapperOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public DocumentMapperNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> DocumentMapperWhere eq(T value) {
        eqImpl(value);
        return this;
    }


    @Override
    public DocumentMapperWhere like(String value) {
        likeImpl(value);
        return this;
    }


    @Override
    public <T> DocumentMapperWhere gt(T value) {
        gtImpl(value);
        return this;
    }


    @Override
    public <T> DocumentMapperWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperWhere lt(T value) {
        ltImpl(value);
        return this;
    }


    @Override
    public <T> DocumentMapperWhere lte(T value) {
        lteImpl(value);
        return this;
    }


    @Override
    public <T> DocumentMapperWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> DocumentMapperWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }

    @Override
    public DocumentMapperNameOrder asc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), Sort.SortType.ASC));
        return this;
    }

    @Override
    public DocumentMapperNameOrder desc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), Sort.SortType.DESC));
        return this;
    }


    @Override
    public DocumentQuery build() {
        return new ArtemisDocumentQuery(sorts, limit, start, condition, documentCollection);
    }

    @Override
    public DocumentQuery build(Pagination pagination) {
        requireNonNull(pagination, "pagination is required");
        return DocumentQueryPagination.of(build(), pagination);
    }

    @Override
    public <T> List<T> execute(DocumentTemplate template) {
        Objects.requireNonNull(template, "template is required");
        return template.select(this.build());
    }

    @Override
    public <T> Optional<T> executeSingle(DocumentTemplate template) {
        Objects.requireNonNull(template, "template is required");
        return template.singleResult(this.build());
    }

    @Override
    public <T> List<T> execute(DocumentTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.select(this.build(pagination));
    }

    @Override
    public <T> Optional<T> executeSingle(DocumentTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.singleResult(this.build(pagination));
    }

    @Override
    public <T> void execute(DocumentTemplateAsync template, Consumer<List<T>> callback) {
        Objects.requireNonNull(template, "template is required");
        Objects.requireNonNull(callback, "callback is required");
        template.select(this.build(), callback);
    }

    @Override
    public <T> void executeSingle(DocumentTemplateAsync template, Consumer<Optional<T>> callback) {
        Objects.requireNonNull(template, "template is required");
        Objects.requireNonNull(callback, "callback is required");
        template.singleResult(this.build(), callback);
    }

    @Override
    public <T> Page<T> page(DocumentTemplate template, Pagination pagination) {
        requireNonNull(pagination, "pagination is required");
        requireNonNull(template, "template is required");
        return template.select(DocumentQueryPagination.of(build(), pagination));
    }

}
