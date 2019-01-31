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

package org.jnosql.query;

/**
 * The exception class to query
 */
public class QueryException extends RuntimeException {

    /**
     * creates an error with an error message
     *
     * @param message the message
     */
    public QueryException(String message) {
        super(message);
    }

    /**
     * A new exception with a Throwable error
     * @param message the message
     * @param exception the exception
     */
    public QueryException(String message, Throwable exception) {
        super(message, exception);
    }
}
