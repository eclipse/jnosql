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

/**
 * An observer for a parser; this observer allows checking both the name of an entity and the fields.
 * This observer might be used for the mapping process.
 */
public interface CommunicationObserverParser {

    /**
     * An empty instance of {@link CommunicationObserverParser}.
     */
    CommunicationObserverParser EMPTY = new CommunicationObserverParser() {
    };

    /**
     * Fires an event for the entity name.
     *
     * @param entity the entity name
     * @return the result of processing the entity name
     * @throws NullPointerException when the entity is null
     */
    default String fireEntity(String entity) {
        return entity;
    }

    /**
     * Fires an event for each field in the mapper process.
     *
     * @param entity the entity name
     * @param field  the field name
     * @return the result of processing the field name
     * @throws NullPointerException when either entity or field is null
     */
    default String fireField(String entity, String field) {
        return field;
    }

}
