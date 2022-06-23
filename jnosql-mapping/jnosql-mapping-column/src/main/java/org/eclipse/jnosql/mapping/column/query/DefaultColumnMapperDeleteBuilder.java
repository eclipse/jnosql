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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperDeleteFrom;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperDeleteNameCondition;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperDeleteNotCondition;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperDeleteWhere;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;

import static java.util.Objects.requireNonNull;

class DefaultColumnMapperDeleteBuilder extends AbstractMapperQuery implements ColumnMapperDeleteFrom,
        ColumnMapperDeleteWhere, ColumnMapperDeleteNameCondition, ColumnMapperDeleteNotCondition {


    DefaultColumnMapperDeleteBuilder(ClassMapping mapping, Converters converters) {
        super(mapping, converters);
    }

    @Override
    public ColumnMapperDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnMapperDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnMapperDeleteNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }


    @Override
    public ColumnMapperDeleteNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public ColumnMapperDeleteWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere lt(T value) {
        ltImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperDeleteWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }


    @Override
    public <T> ColumnMapperDeleteWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    @Override
    public ColumnDeleteQuery build() {
        return new MappingColumnDeleteQuery(columnFamily, condition);
    }

    @Override
    public void delete(ColumnTemplate template) {
        requireNonNull(template, "template is required");
        template.delete(this.build());
    }

}
