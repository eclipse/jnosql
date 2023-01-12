/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.eclipse.jnosql.communication.query;


import org.eclipse.jnosql.communication.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * The select statement reads one or more fields for one or more entities.
 * It returns a result-set of the entities matching the request, where each entity contains the fields
 * for corresponding to the query.
 */
public final  class SelectQuery implements Query {

    private final String entity;

    private final List<String> fields;

    private final List<Sort> sorts;

    private final long skip;

    private final long limit;

    private final Where where;

    SelectQuery(String entity, List<String> fields, List<Sort> sorts, long skip, long limit, Where where) {
        this.entity = entity;
        this.fields = fields;
        this.sorts = sorts;
        this.skip = skip;
        this.limit = limit;
        this.where = where;
    }

    /**
     * The fields that will retrieve in this query, if this fields is empty, this query will retrieve the whole entity.
     *
     * @return the fields list
     */
    public List<String> fields() {
        return fields;
    }

    /**
     * The entity name
     *
     * @return the entity name
     */
    public String entity() {
        return entity;
    }

    /**
     * The condition at this {@link SelectQuery}, if the Where is empty that means may retrieve the whole entities.
     *
     * @return the {@link Where} entity otherwise {@link Optional#empty()}
     */
    public Optional<Where> where() {
        return Optional.ofNullable(where);
    }

    /**
     * Statement defines where the query should start
     *
     * @return the number to skip, otherwise either negative value or zero
     */
    public long skip() {
        return skip;
    }

    /**
     * Statement limits the number of rows returned by a query,
     *
     * @return the maximum of result, otherwise either negative value or zero
     */
    public long limit() {
        return limit;
    }

    /**
     * The list of orders, it is used to sort the result-set in ascending or descending order.
     *
     * @return the order list
     */
    public List<Sort> orderBy() {
        return sorts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SelectQuery)) {
            return false;
        }
        SelectQuery that = (SelectQuery) o;
        return skip == that.skip &&
                limit == that.limit &&
                Objects.equals(entity, that.entity) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(sorts, that.sorts) &&
                Objects.equals(where, that.where);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, fields, sorts, skip, limit, where);
    }
}
