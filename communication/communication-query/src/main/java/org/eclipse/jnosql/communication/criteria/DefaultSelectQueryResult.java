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

import jakarta.nosql.criteria.SelectQueryResult;
import java.util.stream.Stream;

/**
 * Default implementation for {@link SelectQueryResult}
 * This contains the results entiry stream
 *
 * @param <T> the type of the root entity
 */
public class DefaultSelectQueryResult<T> implements SelectQueryResult<T> {
    
    private final Stream<T> results;

    public DefaultSelectQueryResult(Stream<T> results) {
        this.results = results;
    }

    @Override
    public Stream<T> getStream() {
        return this.results;
    }
    
}
