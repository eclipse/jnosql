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
package org.jnosql.artemis.column.query;

import org.jnosql.artemis.Converters;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.diana.api.column.ColumnDeleteQuery;

import java.util.function.Consumer;

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
        return new ArtemisColumnDeleteQuery(columnFamily, condition);
    }

    @Override
    public void execute(ColumnTemplate template) {
        requireNonNull(template, "template is required");
        template.delete(this.build());
    }

    @Override
    public void execute(ColumnTemplateAsync template) {
        requireNonNull(template, "template is required");
        template.delete(this.build());
    }

    @Override
    public void execute(ColumnTemplateAsync template, Consumer<Void> callback) {
        requireNonNull(template, "template is required");
        requireNonNull(callback, "callback is required");
        template.delete(this.build(), callback);
    }

}
