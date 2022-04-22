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

import jakarta.nosql.criteria.AggregatedQuery;
import jakarta.nosql.criteria.Expression;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.jnosql.AbstractGenericType;

public class DefaultAggregatedQuery<T extends Object> extends AbstractGenericType<T> implements AggregatedQuery<T> {

    private final Collection<Expression<T, ?>> groupings;

    public DefaultAggregatedQuery(Class<T> type, Expression<T, ?>... groupings) {
        super(type);
        this.groupings = Arrays.asList(groupings);
    }

    public Collection<Expression<T, ?>> getGroupings() {
        return groupings;
    }

}
