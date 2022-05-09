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
import jakarta.nosql.criteria.Path;
import jakarta.nosql.criteria.StringExpression;
import jakarta.nosql.metamodel.StringAttribute;

/**
 * Default implementation for {@link StringExpression}
 *
 * @param <X> the root type
 * @param <Y> the entity type
 */
public class DefaultStringExpression<X, Y> extends DefaultExpression<X, Y, String> implements StringExpression<X, Y> {

    public DefaultStringExpression(Path<X, Y> path, StringAttribute attribute) {
        super(path, attribute);
    }    
    
    @Override
    public BinaryPredicate<X, String, String> like(String pattern) {
        return new DefaultBinaryPredicate(BinaryPredicate.Operator.LIKE, this, pattern);
    }
    
}
