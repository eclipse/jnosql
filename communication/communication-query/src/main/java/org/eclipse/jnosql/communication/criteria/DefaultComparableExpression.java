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
import jakarta.nosql.criteria.ComparableExpression;
import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.Order;
import jakarta.nosql.criteria.RangePredicate;

public class DefaultComparableExpression<X extends Object, T extends Comparable> extends DefaultExpression<X, T> implements ComparableExpression<X, T> {

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> greaterThan(Expression<X, ? extends Y> expression) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.GREATER_THAN, this, expression);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> greaterThan(Y y) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.GREATER_THAN, this, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> greaterThanOrEqualTo(Expression<X, ? extends Y> expression) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.GREATER_THAN_OR_EQUAL, this, expression);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> greaterThanOrEqualTo(Y y) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.GREATER_THAN_OR_EQUAL, this, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> lessThan(Expression<X, ? extends Y> expression) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.LESS_THAN, this, expression);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> lessThan(Y y) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.LESS_THAN, this, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Expression<X, Y>> lessThanOrEqualTo(Expression<X, ? extends Y> expression) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.LESS_THAN_OR_EQUAL, this, expression);
    }

    @Override
    public <Y extends Comparable<? super Y>> BinaryPredicate<X, Y, Y> lessThanOrEqualTo(Y y) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.LESS_THAN_OR_EQUAL, this, y);
    }

    @Override
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Expression<X, Y>> exclusiveBetween(Expression<X, ? extends Y> from, Expression<X, ? extends Y> to) {
        return new DefaultRangePredicate(RangePredicate.Operator.EXCLUSIVE_BETWEEN, this, from, to);
    }

    @Override
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Y> exclusiveBetween(Y from, Y to) {
        return new DefaultRangePredicate(RangePredicate.Operator.EXCLUSIVE_BETWEEN, this, from, to);
    }

    @Override
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Expression<X, Y>> inclusiveBetween(Expression<X, ? extends Y> from, Expression<X, ? extends Y> to) {
        return new DefaultRangePredicate(RangePredicate.Operator.INCLUSIVE_BETWEEN, this, from, to);
    }

    @Override
    public <Y extends Comparable<? super Y>> RangePredicate<X, Y, Y> inclusiveBetween(Y from, Y to) {
        return new DefaultRangePredicate(RangePredicate.Operator.INCLUSIVE_BETWEEN, this, from, to);
    }

    @Override
    public <T> Order<T> asc() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <T> Order<T> desc() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
