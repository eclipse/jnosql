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
package org.eclipse.jnosql.communication.query;

import jakarta.nosql.query.Condition;
import jakarta.nosql.query.InsertQuery;
import jakarta.nosql.query.JSONQueryValue;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

final class DefaultInsertQuery implements InsertQuery {

    private final String entity;

    private final Duration duration;

    private final List<Condition> conditions;

    private final JSONQueryValue value;

    DefaultInsertQuery(String entity, Duration duration, List<Condition> conditions, JSONQueryValue value) {
        this.entity = entity;
        this.duration = duration;
        this.conditions = conditions;
        this.value = value;
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
    public Optional<JSONQueryValue> getValue() {
        return Optional.ofNullable(value);
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
        if (conditions.isEmpty() && value != null) {
            return "insert " + entity + ' ' + value + ' ' + getTtl().map(Duration::toString).orElse("");
        } else {
            return "insert " + entity + " (" + conditions + ") " + getTtl().map(Duration::toString).orElse("");
        }
    }
}
