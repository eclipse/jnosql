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
import java.util.Collection;

public class DefaultExpression<X extends Object, T extends Object> implements Expression<X, T> {

    @Override
    public BinaryPredicate<X, T, Expression<X, T>> equal(Expression<X, T> expression) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.EQUAL, this, expression);
    }

    @Override
    public BinaryPredicate<X, T, T> equal(T value) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.EQUAL, this, value);
    }

    @Override
    public BinaryPredicate<X, T, Collection<T>> in(Collection<T> values) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.IN, this, values);
    }
    
}
