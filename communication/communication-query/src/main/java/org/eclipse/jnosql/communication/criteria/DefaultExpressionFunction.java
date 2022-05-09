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
import jakarta.nosql.criteria.ExpressionFunction;

/**
 * Default implementation for {@link ExpressionFunction}
 * This holds the function and the expression the function is applied to.
 *
 * @param <X> the root type
 * @param <Y> the entity type
 * @param <T> the type of the expression
 */
public class DefaultExpressionFunction<X, Y, T, R> implements ExpressionFunction<X, Y, T, R> {
    
    private final Expression<X, Y, T> expression;
    private final Function function;

    public DefaultExpressionFunction(Expression<X, Y, T> expression, Function function) {
        this.expression = expression;
        this.function = function;
    }

    @Override
    public Expression<X, Y, T> getExpression() {
        return expression;
    }
    
    @Override
    public Function getFunction() {
        return function;
    }  
    
}
