/*
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
 */
package org.eclipse.jnosql.mapping.document.query;

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.QueryMapper.MapperDeleteFrom;
import jakarta.nosql.mapping.QueryMapper.MapperDeleteNameCondition;
import jakarta.nosql.mapping.QueryMapper.MapperDeleteNotCondition;
import jakarta.nosql.mapping.QueryMapper.MapperDeleteWhere;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import static java.util.Objects.requireNonNull;

class DefaultDocumentMapperDeleteBuilder extends AbstractMapperQuery implements MapperDeleteFrom,
        MapperDeleteWhere, MapperDeleteNameCondition, MapperDeleteNotCondition {


    DefaultDocumentMapperDeleteBuilder(EntityMetadata mapping, Converters converters, DocumentTemplate template) {
        super(mapping, converters, template);
    }

    @Override
    public MapperDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public MapperDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public MapperDeleteNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }


    @Override
    public MapperDeleteNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> MapperDeleteWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public MapperDeleteWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere lt(T value) {
        ltImpl(value);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> MapperDeleteWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    private DocumentDeleteQuery build() {
        return new MappingDocumentDeleteQuery(documentCollection, condition);
    }


    @Override
    public void execute() {
        DocumentDeleteQuery query = build();
        this.template.delete(query);
    }
}