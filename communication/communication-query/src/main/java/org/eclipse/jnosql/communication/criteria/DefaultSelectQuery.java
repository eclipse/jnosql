/*
 *  Copyright (c) 2022 Otávio Santana and others
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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.communication.criteria;

import jakarta.nosql.criteria.Order;
import jakarta.nosql.criteria.SelectQuery;
import jakarta.nosql.criteria.SelectQueryResult;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Default implementation for {@link SelectQuery}
 *
 * @param <X> the type of the root entity
 */
public class DefaultSelectQuery<
        X> extends AbstractRestrictedQuery<X, SelectQueryResult<X>, SelectQuery<X>> implements SelectQuery<X> {

    private List<Order<X, ?>> sortings;
    private Integer maxResults;
    private Integer firstResult;
    private DefaultSelectQueryResult<X> result;

    public DefaultSelectQuery(Class<X> type) {
        super(type);
    }

    @Override
    public SelectQuery<X> orderBy(Order<X, ?>... sortings) {
        this.sortings = Arrays.asList(sortings);
        return this;
    }

    @Override
    public List<Order<X, ?>> getOrderBy() {
        return this.sortings;
    }

    @Override
    public SelectQuery<X> setMaxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    @Override
    public int getMaxResults() {
        return maxResults;
    }

    @Override
    public SelectQuery<X> setFirstResult(int firstResult) {
        this.firstResult = firstResult;
        return this;
    }

    @Override
    public int getFirstResult() {
        return firstResult;
    }

    @Override
    public SelectQuery<X> feed(Stream<X> results) {
        this.result = new DefaultSelectQueryResult(results);
        return this;
    }

    @Override
    public SelectQueryResult<X> getResult() {
        return this.result;
    }

}
