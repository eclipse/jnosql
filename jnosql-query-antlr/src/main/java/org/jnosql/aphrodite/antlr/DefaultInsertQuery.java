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
import org.jnosql.query.InsertQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

final class DefaultInsertQuery implements InsertQuery {

    private final String entity;

    private final Duration duration;

    private final List<Condition> conditions;

    DefaultInsertQuery(String entity, Duration duration, List<Condition> conditions) {
        this.entity = entity;
        this.duration = duration;
        this.conditions = conditions;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public Optional<Duration> getTtl() {
        return Optional.ofNullable(duration);
    }

    @Override
    public List<Condition> getConditions() {
        return unmodifiableList(conditions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultInsertQuery)) {
            return false;
        }
        DefaultInsertQuery that = (DefaultInsertQuery) o;
        return Objects.equals(entity, that.entity) &&
                Objects.equals(duration, that.duration) &&
                Objects.equals(conditions, that.conditions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, duration, conditions);
    }

    @Override
    public String toString() {
        return "update " + entity + " (" + conditions + ") " + getTtl().map(Duration::toString).orElse("");
    }
}
