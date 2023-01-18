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
package org.eclipse.jnosql.communication.query.method;


import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.eclipse.jnosql.communication.query.Where;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class MethodDeleteQuery implements DeleteQuery {

    private final String entity;

    private final Where where;

    MethodDeleteQuery(String entity, Where where) {
        this.entity = entity;
        this.where = where;
    }

    @Override
    public List<String> fields() {
        return Collections.emptyList();
    }

    @Override
    public String entity() {
        return entity;
    }

    @Override
    public Optional<Where> where() {
        return Optional.ofNullable(where);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MethodDeleteQuery that = (MethodDeleteQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(where, that.where);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, where);
    }

    @Override
    public String toString() {
        return entity + " where " + where;
    }
}
