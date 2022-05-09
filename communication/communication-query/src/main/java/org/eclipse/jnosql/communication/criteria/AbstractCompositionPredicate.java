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

import jakarta.nosql.criteria.CompositionPredicate;
import jakarta.nosql.criteria.Predicate;
import java.util.Collection;

/**
 * Abstract class to extend for composing {@link Predicate}s
 *
 * @param <X> The Entity type whose fetching is to be be restricted
 */
public abstract class AbstractCompositionPredicate<X> extends AbstractPredicate<X> implements CompositionPredicate<X> {

    private final Collection<Predicate<X>> predicates;

    public AbstractCompositionPredicate(Collection<Predicate<X>> predicates) {
        this.predicates = predicates;
    }

    @Override
    public Collection<Predicate<X>> getPredicates() {
        return this.predicates;
    }

}
