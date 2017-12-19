/*
 *
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
 *
 */
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentQuery;

import static java.util.Objects.requireNonNull;

class DefaultDocumentWhere implements DocumentWhere {

    private final DefaultSelectQueryBuilder queryBuilder;

    public DefaultDocumentWhere(DefaultSelectQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }


    @Override
    public DocumentNameCondition and(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.queryBuilder.name = name;
        this.queryBuilder.and = true;
        return queryBuilder;
    }

    @Override
    public DocumentNameCondition or(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.queryBuilder.name = name;
        this.queryBuilder.and = false;
        return queryBuilder;
    }

    @Override
    public DocumentStart start(long start) {
        this.queryBuilder.start(start);
        return queryBuilder;
    }

    @Override
    public DocumentLimit limit(long limit) {
        this.queryBuilder.limit(limit);
        return queryBuilder;
    }

    @Override
    public DocumentOrder orderBy(String name) throws NullPointerException {
        this.queryBuilder.orderBy(name);
        return queryBuilder;
    }

    @Override
    public DocumentQuery build() {
        return queryBuilder.build();
    }
}
