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

import java.util.function.Supplier;

/**
 * When The Entity is converted to communication layer,
 * this entity must have a field with {@link org.jnosql.artemis.Id} annotation. If this entity
 * hasn't this information an exception will be launch.
 */
public class IdNotFoundException extends ArtemisException {

    public static final Supplier<IdNotFoundException> KEY_NOT_FOUND_EXCEPTION_SUPPLIER = ()
            -> new IdNotFoundException("To use this resource you must annotaded a fiel with @org.jnosql.artemis.Id");
    /**
     * New exception instance with the exception message
     *
     * @param message the exception message
     */
    public IdNotFoundException(String message) {
        super(message);
    }


    public static IdNotFoundException newInstance(Class<?> clazz) {
        String message = "The entity " + clazz + " must have a field annoted with @Id";
        return new IdNotFoundException(message);
    }
}
