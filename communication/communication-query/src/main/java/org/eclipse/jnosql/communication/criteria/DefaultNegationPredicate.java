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

import jakarta.nosql.criteria.NegationPredicate;
import jakarta.nosql.criteria.Predicate;

/**
 * Default implementation for {@link NegationPredicate}
 * This holds the predicate to negate.
 *
 * @param <X> The Entity type whose fetching is to be be restricted
 */
public class DefaultNegationPredicate<X> extends AbstractPredicate<X> implements NegationPredicate<X> {

    private final Predicate<X> predicate;

    public DefaultNegationPredicate(Predicate<X> predicate) {
        this.predicate = predicate;
    }
    
    @Override
    public Predicate<X> getPredicate() {
        return this.predicate;
    }    
    
}
