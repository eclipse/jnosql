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
import jakarta.nosql.criteria.Path;
import jakarta.nosql.criteria.StringExpression;
import jakarta.nosql.metamodel.ComparableAttribute;
import jakarta.nosql.metamodel.EntityAttribute;
import jakarta.nosql.metamodel.StringAttribute;
import jakarta.nosql.metamodel.ValueAttribute;
import org.eclipse.jnosql.AbstractGenericType;
import jakarta.nosql.criteria.ComparableExpression;
import jakarta.nosql.criteria.NumberExpression;
import jakarta.nosql.metamodel.NumberAttribute;

public class DefaultPath<X extends Object, Y extends Object> extends AbstractGenericType<X> implements Path<X, Y> {

    public DefaultPath(Class<X> type) {
        super(type);
    }

    @Override
    public <Z> Path<X, Z> get(EntityAttribute<? super Y, Z> attribute) {
        return new DefaultPath<>(this.getType());
    }

    @Override
    public <Z> Expression<X, Z> get(ValueAttribute<? super Y, Z> attribute) {
        return new DefaultExpression<>();
    }

    @Override
    public StringExpression<X> get(StringAttribute<? extends Y> attribute) {
        return new DefaultStringExpression<>();
    }

    @Override
    public <Z extends Comparable> ComparableExpression<X, Z> get(ComparableAttribute<? super Y, Z> attribute) {
        return new DefaultComparableExpression<>();
    }

    @Override
    public <Z extends Number & Comparable> NumberExpression<X, Z> get(NumberAttribute<? super Y, Z> attribute) {
        return new DefaultNumberExpression<>();
    }
    
}
