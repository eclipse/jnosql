/*
 * Copyright 2017 Eclipse Foundation
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

/**
 * The root exception to async query
 */
public class ExecuteAsyncQueryException extends JNoSQLException {


    /**
     * Constructs a new runtime exception with null as its detail message.
     */
    public ExecuteAsyncQueryException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     *
     * @param message
     */
    public ExecuteAsyncQueryException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ExecuteAsyncQueryException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new runtime exception with the specified cause and a detail
     * message of (cause==null ? null : cause.toString()) (which typically contains
     * the class and detail message of cause).
     *
     * @param cause the cause
     */
    public ExecuteAsyncQueryException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime exception with the specified detail
     * message, cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message            the message
     * @param cause              the cause
     * @param enableSuppression  the enableSuppression
     * @param writableStackTrace the writableStackTrace
     */
    protected ExecuteAsyncQueryException(String message, Throwable cause,
                              boolean enableSuppression,
                              boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
