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
import jakarta.nosql.metamodel.Attribute;
import jakarta.nosql.metamodel.NumberAttribute;

public class DefaultPath<X, Y> extends AbstractGenericType<X> implements Path<X, Y> {

    private Path<X, ?> parent;

    private Attribute<?, Y> attribute;
    
    public DefaultPath(Class<X> type) {
        super(type);
    }

    public DefaultPath(Class<X> type, Path<X, ?> parent, Attribute<?, Y> attribute) {
        super(type);
        this.parent = parent;
        this.attribute = attribute;
    }

    @Override
    public Path<X, ?> getParent() {
        return this.parent;
    }

    @Override
    public Attribute<?, Y> getAttribute() {
        return this.attribute;
    }

    @Override
    public <Z> Path<X, Z> get(EntityAttribute<Y, Z> attribute) {
        return new DefaultPath<>(this.getType(), this, attribute);
    }

    @Override
    public <Z> Expression<X, Y, Z> get(ValueAttribute<Y, Z> attribute) {
        return new DefaultExpression<>(this, attribute);
    }

    @Override
    public StringExpression<X, Y> get(StringAttribute<Y> attribute) {
        return new DefaultStringExpression<>(this, attribute);
    }

    @Override
    public <Z extends Comparable> ComparableExpression<X, Y, Z> get(ComparableAttribute<Y, Z> attribute) {
        return new DefaultComparableExpression<>(this, attribute);
    }

    @Override
    public <Z extends java.lang.Number & java.lang.Comparable> NumberExpression<X, Y, Z> get(NumberAttribute<Y, Z> attribute) {
        return new DefaultNumberExpression<>(this, attribute);
    }
    
}
