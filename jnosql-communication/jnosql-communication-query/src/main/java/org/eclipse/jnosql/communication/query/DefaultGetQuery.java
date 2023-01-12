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

import java.util.List;
import java.util.Objects;

import static java.util.Collections.unmodifiableList;


/**
 * To delete one or more entities use the <b>DEL</b> statement.
 * This query is particular to a key-value database.
 */
public final class DefaultGetQuery implements Query {

    private final List<QueryValue<?>> keys;

    DefaultGetQuery(List<QueryValue<?>> keys) {
        this.keys = keys;
    }

    /**
     * The keys to being removed from the query
     * @return the keys
     */
    public List<QueryValue<?>> keys() {
        return unmodifiableList(keys);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultGetQuery)) {
            return false;
        }
        DefaultGetQuery that = (DefaultGetQuery) o;
        return Objects.equals(keys, that.keys);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(keys);
    }

    @Override
    public String toString() {
        return this.keys.toString();
    }
}
