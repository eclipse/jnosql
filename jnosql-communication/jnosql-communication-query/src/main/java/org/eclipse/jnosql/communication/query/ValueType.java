/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

/**
 * The values type that the query supports at this syntax.
 */
public enum ValueType {
    /**
     * Conditions are statements that are created by the programmer which evaluates
     * actions in the program and evaluates if it's true or false.
     */
    CONDITION,
    /**
     * The values that implements {@link Number}
     */
    NUMBER,
    /**
     * The values that implements {@link CharSequence} in general it will be {@link String}
     */
    STRING,
    /**
     * That parameter is for dynamic query where you define prepare statement.
     */
    PARAMETER,
    /**
     * An array is a series of memory locations
     */
    ARRAY,
    /**
     *This type defines a task to be defined on query.
     */
    FUNCTION,
    /**
     * JSON is an open standard file format and data interchange
     * format that uses human-readable text to store and transmit data objects consisting
     * of attribute–value pairs and arrays.
     */
    JSON,
    /**
     * In computer science, a Boolean is a logical data type that can have only the values true or false
     */
    BOOLEAN;
}
