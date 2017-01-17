/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api;

import java.util.Arrays;
import java.util.Objects;

/**
 * Conditions type to run a query
 */
public enum Condition {
    EQUALS, GREATER_THAN, GREATER_EQUALS_THAN, LESSER_THAN, LESSER_EQUALS_THAN, IN, LIKE, AND, OR, NOT;

    /**
     * Return tne field as name to both document and column.
     * The goal is the field gonna be a reserved word.
     * The foruma is: underscore plus the {@link Enum#name()}
     * So, the {@link Condition#EQUALS#getNameField()} will return "_EQUALS"
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
