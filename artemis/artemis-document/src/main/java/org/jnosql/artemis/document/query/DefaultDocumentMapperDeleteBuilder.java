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

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteFrom;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteNameCondition;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteNotCondition;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteWhere;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import jakarta.nosql.mapping.reflection.ClassMapping;

import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

class DefaultDocumentMapperDeleteBuilder extends AbstractMapperQuery implements DocumentMapperDeleteFrom,
        DocumentMapperDeleteWhere, DocumentMapperDeleteNameCondition, DocumentMapperDeleteNotCondition {


    DefaultDocumentMapperDeleteBuilder(ClassMapping mapping, Converters converters) {
        super(mapping, converters);
    }

    @Override
    public DocumentMapperDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public DocumentMapperDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public DocumentMapperDeleteNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }


    @Override
    public DocumentMapperDeleteNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public DocumentMapperDeleteWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere lt(T value) {
        ltImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> DocumentMapperDeleteWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }


    @Override
    public <T> DocumentMapperDeleteWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    @Override
    public DocumentDeleteQuery build() {
        return new ArtemisDocumentDeleteQuery(documentCollection, condition);
    }

    @Override
    public void execute(DocumentTemplate template) {
        requireNonNull(template, "template is required");
        template.delete(this.build());
    }

    @Override
    public void execute(DocumentTemplateAsync template) {
        requireNonNull(template, "template is required");
        template.delete(this.build());
    }

    @Override
    public void execute(DocumentTemplateAsync template, Consumer<Void> callback) {
        requireNonNull(template, "template is required");
        requireNonNull(callback, "callback is required");
        template.delete(this.build(), callback);
    }


}