/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.diana.api.encryption;

import org.jnosql.diana.api.JNoSQLException;

/**
 * An exception specific to property encryption/decryption process
 */
public class EncryptionException extends JNoSQLException {

    /**
     * Constructs a new runtime exception with the specified detail message and cause.
     *
     * @param message the message
     * @param cause   the cause
     */
    EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
