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

import jakarta.nosql.criteria.BinaryPredicate;
import jakarta.nosql.criteria.Expression;

public class DefaultBinaryPredicate<X, LHS, RHS> extends AbstractPredicate<X> implements BinaryPredicate<X, LHS, RHS> {

    private final Operator operator;
    private final Expression<X, ?, LHS> left;
    private final RHS right;
    
    public DefaultBinaryPredicate(Operator operator, Expression<X, ?, LHS> left, RHS right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }

    @Override
    public Operator getOperator() {
        return this.operator;
    }

    @Override
    public Expression<X, ?, LHS> getLeft() {
        return left;
    }

    @Override
    public RHS getRight() {
        return right;
    }
    
}
