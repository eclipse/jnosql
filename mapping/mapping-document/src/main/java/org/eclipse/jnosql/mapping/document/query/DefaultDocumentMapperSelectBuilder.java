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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperFrom;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperLimit;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperNameCondition;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperNameOrder;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperNotCondition;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperOrder;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperSkip;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperWhere;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

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
        this.sorts.add(Sort.of(mapping.getColumnField(name), SortType.ASC));
        return this;
    }

    @Override
    public DocumentMapperNameOrder desc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), SortType.DESC));
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
    public <T> Stream<T> getResult(DocumentTemplate template) {
        Objects.requireNonNull(template, "template is required");
        return template.select(this.build());
    }

    @Override
    public <T> Optional<T> getSingleResult(DocumentTemplate template) {
        Objects.requireNonNull(template, "template is required");
        return template.singleResult(this.build());
    }

    @Override
    public <T> Stream<T> getResult(DocumentTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.select(this.build(pagination));
    }

    @Override
    public <T> Optional<T> getSingleResult(DocumentTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.singleResult(this.build(pagination));
    }

    @Override
    public <T> Page<T> page(DocumentTemplate template, Pagination pagination) {
        requireNonNull(pagination, "pagination is required");
        requireNonNull(template, "template is required");
        return template.select(DocumentQueryPagination.of(build(), pagination));
    }

}
