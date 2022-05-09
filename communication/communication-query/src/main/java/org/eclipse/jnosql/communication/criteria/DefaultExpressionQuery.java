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

import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.ExpressionQuery;
import java.util.Arrays;
import java.util.Collection;

public class DefaultExpressionQuery<T>
        extends DefaultSelectQuery<T, DefaultExpressionQuery<T>> implements ExpressionQuery<T> {

    private final Collection<Expression<T, ?, ?>> expressions;

    public DefaultExpressionQuery(Class<T> type, Expression<T, ?, ?>... expressions) {
        super(type);
        this.expressions = Arrays.asList(expressions);
    }

    @Override
    public Collection<Expression<T, ?, ?>> getExpressions() {
        return expressions;
    }
    
}
