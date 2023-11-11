/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.eclipse.jnosql.communication.query;


import jakarta.data.repository.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * The default implementation of {@link SelectQuery}
 */
record DefaultSelectQuery(String entity, List<String> fields, List<Sort> orderBy, long skip,
                               long limit, Where condition) implements SelectQuery {


    @Override
    public Optional<Where> where() {
        return Optional.ofNullable(condition);
    }

    /**
     * Obtains an instance of {@link DefaultSelectQuery} from a text string.
     *
     * @param query the query
     * @return {@link DefaultSelectQuery} instance
     * @throws NullPointerException when the query is null
     */
    static DefaultSelectQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return new SelectQueryConverter().apply(query);
    }
}
