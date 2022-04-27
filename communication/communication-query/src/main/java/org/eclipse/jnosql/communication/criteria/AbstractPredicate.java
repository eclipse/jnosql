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

import jakarta.nosql.criteria.ConjunctionPredicate;
import jakarta.nosql.criteria.DisjunctionPredicate;
import jakarta.nosql.criteria.NegationPredicate;
import jakarta.nosql.criteria.Predicate;
import java.util.Arrays;

public abstract class AbstractPredicate<T> implements Predicate<T>{
    
    @Override
    public NegationPredicate<T> not() {
        return new DefaultNegationPredicate(this);
    }

    @Override
    public ConjunctionPredicate<T> and(Predicate<T> restriction) {
        return new DefaultConjunctionPredicate<>(
                Arrays.asList(
                        this,
                        restriction
                )
        );
    }

    @Override
    public DisjunctionPredicate<T> or(Predicate<T> restriction) {
        return new DefaultDisjunctionPredicate<>(
                Arrays.asList(
                        this,
                        restriction
                )
        );
    }
    
}
