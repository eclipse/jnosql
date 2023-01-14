/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.graph;


import jakarta.data.exceptions.MappingException;

/**
 * An exception that provides information when executing Gremlin in the database.
 */
public class GremlinQueryException extends MappingException {

    /**
     * A new instance with both the cause of the error and a message
     *
     * @param message the message
     * @param cause   the cause
     */
    public GremlinQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * A new instance with the message
     *
     * @param message the message
     */
    public GremlinQueryException(String message) {
        super(message);
    }
}
