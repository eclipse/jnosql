/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping;


import jakarta.nosql.NoSQLException;

/**
 * The root exception to dynamic query on {@link jakarta.data.repository.CrudRepository}
 */
public class DynamicQueryException extends NoSQLException {

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message the message
     */
    public DynamicQueryException(String message) {
        super(message);
    }
}
