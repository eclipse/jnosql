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
import jakarta.nosql.criteria.CriteriaFunction;
import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.FunctionQuery;
import java.util.Arrays;
import java.util.Collection;

public class DefaultFunctionQuery<T extends Object> extends AbstractRestrictedQuery<T, DefaultFunctionQueryResult<T>, DefaultFunctionQuery<T>> implements FunctionQuery<T> {

    private final Collection<CriteriaFunction<T, ?, ?>> functions;

    public DefaultFunctionQuery(Class<T> type, CriteriaFunction<T, ?, ?>... functions) {
        super(type);
        this.functions = Arrays.asList(functions);
    }

    public Collection<CriteriaFunction<T, ?, ?>> getFunctions() {
        return functions;
    }

    @Override
    public AggregatedQuery<T> groupBy(Expression<T, ?>... groupings) {
        return new DefaultAggregatedQuery(this.getType(), groupings);
    }

}
