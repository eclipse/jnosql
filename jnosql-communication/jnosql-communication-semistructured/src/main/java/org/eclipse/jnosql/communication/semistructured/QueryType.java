/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import java.util.Objects;

/**
 * Enum representing the different types of queries supported in Jakarta Data.
 *
 * <p>The {@code QueryType} enum categorizes queries into three main types: {@code SELECT},
 * {@code DELETE}, and {@code UPDATE}. These types correspond to the standard operations
 * typically executed against a database. This enum is used to interpret and classify
 * queries within the Jakarta Data API, particularly in implementations like Eclipse JNoSQL.</p>
 *
 * <ul>
 *   <li>{@link #SELECT} - Represents a query that retrieves data from the database.</li>
 *   <li>{@link #DELETE} - Represents a query that removes data from the database.</li>
 *   <li>{@link #UPDATE} - Represents a query that modifies existing data in the database.</li>
 * </ul>
 *
 * <p>The {@link #parse(String)} method is provided to determine the type of a given query
 * string by extracting and evaluating its command keyword. The method returns a corresponding
 * {@code QueryType} based on the first six characters of the query, assuming that the query
 * begins with a standard SQL-like command.</p>
 *
 * <p>Note that if the query string does not contain a recognizable command (e.g., if it is
 * shorter than six characters or does not match any known command), the method defaults to
 * {@code SELECT}.</p>
 *
 * <p>This enum is particularly relevant for NoSQL implementations like Eclipse JNoSQL, where
 * the query language might differ from traditional SQL, yet still, adhere to the concepts
 * of selection, deletion, and updating of data.</p>
 */
public enum QueryType {

    /**
     * Represents a query that retrieves data from the database.
     * This is the default query type when no specific command is recognized.
     */
    SELECT,

    /**
     * Represents a query that removes data from the database.
     * Typically used to delete one or more records based on certain conditions.
     */
    DELETE,

    /**
     * Represents a query that modifies existing data in the database.
     * Typically used to update one or more records based on certain conditions.
     */
    UPDATE;

    /**
     * Parses the given query string to determine the type of query.
     *
     * @param query the query string to parse
     * @return the {@code QueryType} corresponding to the query command
     */
    public static QueryType parse(String query) {
        Objects.requireNonNull(query, "Query string cannot be null");
        String command = QueryType.extractQueryCommand(query);
        return switch (command) {
            case "DELETE" -> DELETE;
            case "UPDATE" -> UPDATE;
            default -> SELECT;
        };
    }

    private static String extractQueryCommand(String query){
        if(query.length() < 6){
            return "";
        }
        return query.substring(0, 6).toUpperCase();
    }
}
