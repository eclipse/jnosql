/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr;

import org.jnosql.query.Condition;
import org.jnosql.query.UpdateQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class DefaultUpdateQuery implements UpdateQuery {

    private final String entity;

    private final List<Condition> conditions;

    DefaultUpdateQuery(String entity, List<Condition> conditions) {
        this.entity = entity;
        this.conditions = conditions;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public List<Condition> getConditions() {
        return Collections.unmodifiableList(conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultUpdateQuery)) {
            return false;
        }
        DefaultUpdateQuery that = (DefaultUpdateQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, conditions);
    }

    @Override
    public String toString() {
        return "update " + entity + " (" + conditions + ") ";
    }
}
