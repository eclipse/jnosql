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

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;

/**
 * Inserting data for an entity is done using an <b>INSERT</b> statement.
 */
public final class InsertQuery implements Query {

    private final String entity;

    private final Duration duration;

    private final List<QueryCondition> conditions;

    private final JSONQueryValue value;

    InsertQuery(String entity, Duration duration, List<QueryCondition> conditions, JSONQueryValue value) {
        this.entity = entity;
        this.duration = duration;
        this.conditions = conditions;
        this.value = value;
    }

    /**
     * The entity name
     *
     * @return the entity
     */
    public String entity() {
        return entity;
    }

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     *
     * @return the TTL otherwise {@link Optional#empty()}
     */
    public Optional<Duration> ttl() {
        return Optional.ofNullable(duration);
    }

    /**
     * The list of changes as conditions. Each condition will use the equals operator, {@link org.eclipse.jnosql.communication.Condition#EQUALS},
     * e.g., name = "any name"
     *
     * @return the conditions
     */
    public List<QueryCondition> conditions() {
        return unmodifiableList(conditions);
    }

    /**
     * Returns the value to insert when the query uses JSON value instead of Conditions.
     * In an insert, an operation is not able to use both: {@link InsertQuery#conditions()} and
     * {@link InsertQuery#value()}.
     * Therefore, execution will use just one operation type.
     *
     * @return a {@link JSONQueryValue} or {@link Optional#empty()} when it uses {@link InsertQuery#conditions()}
     */
    public Optional<JSONQueryValue> value() {
        return Optional.ofNullable(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InsertQuery)) {
            return false;
        }
        InsertQuery that = (InsertQuery) o;
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
            return "insert " + entity + ' ' + value + ' ' + ttl().map(Duration::toString).orElse("");
        } else {
            return "insert " + entity + " (" + conditions + ") " + ttl().map(Duration::toString).orElse("");
        }
    }

    /**
     * Obtains an instance of {@link InsertQueryProvider} from a text string.
     *
     * @param query the query
     * @return {@link InsertQueryProvider} instance
     * @throws NullPointerException when the query is null
     */
    public static InsertQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return new InsertQueryProvider().apply(query);
    }
}