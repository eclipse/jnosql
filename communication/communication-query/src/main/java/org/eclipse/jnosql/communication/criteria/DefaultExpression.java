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
import jakarta.nosql.criteria.Path;
import jakarta.nosql.metamodel.Attribute;
import java.util.Collection;

/**
 * Default implementation for {@link Expression}
 * This holds the attribute and the path to the entity the attribute belongs to.
 *
 * @param <X> the root type
 * @param <Y> the entity type
 * @param <T> the type of the expression
 */
public class DefaultExpression<X, Y, T> implements Expression<X, Y, T> {
    
    private final Path<X, Y> path;
    private final Attribute<Y, T> attribute;

    public DefaultExpression(Path<X, Y> path, Attribute<Y, T> attribute) {
        this.path = path;
        this.attribute = attribute;
    }
    
    @Override
    public Path<X, Y> getPath() {
        return path;
    }

    @Override
    public Attribute<Y, T> getAttribute() {
        return attribute;
    }

    @Override
    public BinaryPredicate<X, T, Expression<X, Y, T>> equal(Expression<X, Y, T> expression) {
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
