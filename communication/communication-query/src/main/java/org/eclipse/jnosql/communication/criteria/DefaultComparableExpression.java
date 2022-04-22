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

import jakarta.nosql.criteria.ComparableExpression;
import jakarta.nosql.criteria.Expression;
import jakarta.nosql.criteria.Order;
import jakarta.nosql.criteria.Predicate;

public class DefaultComparableExpression<X extends Object, T extends Comparable> extends DefaultExpression<X, T> implements ComparableExpression<X, T> {

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> greaterThan(Expression<X, ? extends Y> expression) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> greaterThan(Y y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> greaterThanOrEqualTo(Expression<X, ? extends Y> expression) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> greaterThanOrEqualTo(Y y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> lessThan(Expression<X, ? extends Y> expression) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> lessThan(Y y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> lessThanOrEqualTo(Expression<X, ? extends Y> expression) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> lessThanOrEqualTo(Y y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> between(Expression<X, ? extends Y> exprsn1, Expression<X, ? extends Y> exprsn2) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public <Y extends Comparable<? super Y>> Predicate<X> between(Y x, Y y) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
