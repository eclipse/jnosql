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
package org.eclipse.jnosql.mapping.validation;


/**
 * Validates bean instances. Implementations of this interface must be thread-safe.
 */
public interface MappingValidator {


    /**
     * Validate an entity using entity validation
     *
     * @param entity the entity to be validated
     * @param <T>  the type
     * @throws NullPointerException       when entity is null
     * @throws javax.validation.ConstraintViolationException when {@link javax.validation.Validator#validate(Object, Class[])}
     *                                    returns a non empty collection
     */
    <T> void validate(T entity);
}
