/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis;

/**
 * Thrown by the persistence provider when an entity does not exist.
 */
public class EntityNotFoundException extends ArtemisException {

    /**
     * Constructs a new EntityNotFoundException exception with the specified detail message.
     *
     * @param message the message
     */
    public EntityNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a new EntityNotFoundException exception with  null as its detail message.
     */
    public EntityNotFoundException() {
        super();
    }
}
