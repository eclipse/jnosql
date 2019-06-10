/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr.method;

import org.jnosql.query.SelectQuery;
import org.jnosql.query.Sort;
import org.jnosql.query.Where;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class MethodSelectQuery implements SelectQuery {

    private final String entity;

    private final Where where;

    private final List<Sort> sorts;

    MethodSelectQuery(String entity, List<Sort> sorts, Where where) {
        this.entity = entity;
        this.sorts = sorts;
        this.where = where;
    }


    @Override
    public List<String> getFields() {
        return Collections.emptyList();
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public Optional<Where> getWhere() {
        return Optional.ofNullable(where);
    }

    @Override
    public long getSkip() {
        return 0;
    }

    @Override
    public long getLimit() {
        return 0;
    }

    @Override
    public List<Sort> getOrderBy() {
        return Collections.unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodSelectQuery that = (MethodSelectQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(where, that.where);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, where);
    }

    @Override
    public String toString() {
        return entity + " where " + where + " orderBy " + sorts;
    }
}
