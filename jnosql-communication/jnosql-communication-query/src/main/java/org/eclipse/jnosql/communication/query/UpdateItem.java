/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 * Represents a single item within an update operation on a NoSQL database. Each {@code UpdateItem}
 * specifies a field in an entity and the new value that the field should be set to during the update.
 * This interface is utilized within update queries to define the changes to be applied to the entities
 * that meet the specified conditions.
 */
public interface UpdateItem {

    /**
     * Retrieves the name of the field to be updated. This name corresponds to a specific attribute
     * or property of an entity that is targeted by the update query.
     *
     * @return the name of the field as a {@code String}. This name is typically the key or identifier
     *         used within the entity's data structure.
     */
    String name();

    /**
     * Retrieves the new value to be set for the field specified by {@link #name()}. This value can be of
     * any type, encapsulated within a {@link QueryValue} to provide flexibility in specifying complex
     * or simple values, including nested objects, arrays, or primitive types.
     *
     * @return a {@link QueryValue} object that encapsulates the new value for the field. The type parameter
     *         of {@code QueryValue} can vary depending on the data type of the field being updated.
     */
    QueryValue<?> value();
}