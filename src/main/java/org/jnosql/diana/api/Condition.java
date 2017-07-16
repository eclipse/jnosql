/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *   Otavio Santana
 *
 */

package org.jnosql.diana.api;

import java.util.Arrays;
import java.util.Objects;

/**
 * Conditions type to run a query
 */
public enum Condition {
    EQUALS, GREATER_THAN, GREATER_EQUALS_THAN, LESSER_THAN, LESSER_EQUALS_THAN, IN, LIKE, AND, OR, NOT, BETWEEN;

    /**
     * Return tne field as name to both document and column.
     * The goal is the field gonna be a reserved word.
     * The foruma is: underscore plus the {@link Enum#name()}
     * So, call this method on {@link Condition#EQUALS}  will return "_EQUALS"
     *
     * @return the keyword to condition
     */
    public String getNameField() {
        return '_' + this.name();
    }

    /**
     * Retrieve the condition from {@link Condition#getNameField()} on case sentive
     *
     * @param condition the condition converted to field
     * @return the condition instance
     * @throws NullPointerException     when condition is null
     * @throws IllegalArgumentException when the condition is not found
     */
    public static Condition parse(String condition) throws NullPointerException, IllegalArgumentException {
        Objects.requireNonNull(condition, "condition is required");
        return Arrays.stream(Condition.values())
                .filter(c -> c.getNameField()
                        .equals(condition)).findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(String.format("The condition $s is not found", condition)));
    }
}
