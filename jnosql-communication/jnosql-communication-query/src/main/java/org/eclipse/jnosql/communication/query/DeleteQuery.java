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


import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Deleting either an entity or fields uses the <b>DELETE</b> statement
 */
public final class DeleteQuery {

    private final String entity;

    private final List<String> fields;

    private final Where where;

    DeleteQuery(String entity, List<String> fields, Where where) {
        this.entity = entity;
        this.fields = fields;
        this.where = where;
    }

    /**
     * The fields that will delete in this query, if this fields is empty, this query will remove the whole entity.
     *
     * @return the fields list
     */
    public List<String> getFields() {
        return Collections.unmodifiableList(fields);
    }

    /**
     * The entity name
     *
     * @return the entity name
     */
    public String getEntity() {
        return entity;
    }

    /**
     * The condition at this {@link DeleteQuery}, if the Where is empty that means will delete the whole entities.
     *
     * @return the {@link Where} entity otherwise {@link Optional#empty()}
     */
    public Optional<Where> getWhere() {
        return Optional.ofNullable(where);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeleteQuery)) {
            return false;
        }
        DeleteQuery that = (DeleteQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(where, that.where);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, fields, where);
    }

    @Override
    public String toString() {
        return "DeleteQuery{" +
                "entity='" + entity + '\'' +
                ", fields=" + fields +
                ", where=" + where +
                '}';
    }
}
