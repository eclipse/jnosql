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
import jakarta.nosql.criteria.RangePredicate;

public class DefaultRangePredicate<X, LHS, RHS> extends AbstractPredicate<X> implements RangePredicate<X, LHS, RHS> {

    private final Operator operator;
    private final Expression<X, ?, LHS> left;
    private final RHS from;
    private final RHS to;
    
    public DefaultRangePredicate(Operator operator, Expression<X, ?, LHS> left, RHS from, RHS to) {
        this.operator = operator;
        this.left = left;
        this.from = from;
        this.to = to;
    }
    
    @Override
    public Operator getOperator() {
        return this.operator;
    }

    @Override
    public Expression<X, ?, LHS> getLeft() {
        return this.left;
    }

    @Override
    public RHS getFrom() {
        return this.from;
    }

    @Override
    public RHS getTo() {
        return this.to;
    }
    
}
