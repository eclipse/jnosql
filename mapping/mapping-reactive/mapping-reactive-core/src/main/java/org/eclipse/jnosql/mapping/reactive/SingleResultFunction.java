/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.reactive;

import jakarta.nosql.NonUniqueResultException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class SingleResultFunction<U> implements Function<List<U>, Optional<U>> {
    @Override
    public Optional<U> apply(List<U> entities) {
        if (entities.size() == 0) {
            return Optional.empty();
        } else if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }
        throw new NonUniqueResultException("There is more than one element found in this query");
    }
}
