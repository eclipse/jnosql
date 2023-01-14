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
package org.eclipse.jnosql.mapping.validation;


import org.eclipse.jnosql.mapping.reflection.ConstructorEvent;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Validates bean instances. Implementations of this interface must be thread-safe.
 */
@ApplicationScoped
public class MappingValidator {

    @Inject
    private Instance<ValidatorFactory> validatorFactories;

    @Inject
    private Instance<Validator> validators;

    /**
     * Validate an entity using entity validation
     *
     * @param entity the entity to be validated
     * @throws NullPointerException                            when entity is null
     * @throws jakarta.validation.ConstraintViolationException when {@link jakarta.validation.Validator#validate(Object, Class[])}
     *                                                         returns a non-empty collection
     */
    public void validate(Object entity) {
        Objects.requireNonNull(entity, "entity is required");
        Validator validator = getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

    }

    /**
     * Validate an entity using entity validation
     *
     * @param event the event
     * @throws NullPointerException                            when entity is null
     * @throws jakarta.validation.ConstraintViolationException when {@link jakarta.validation.Validator#validate(Object, Class[])}
     *                                                         returns a non-empty collection
     */
    public void validate(ConstructorEvent event) {
        Objects.requireNonNull(event, "event is required");
        Validator validator = getValidator();
        ExecutableValidator executableValidator = validator.forExecutables();
        Set<? extends ConstraintViolation<?>> violations =
                executableValidator.validateConstructorParameters(event.getConstructor(),
                        event.getParams());

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }

    private Validator getValidator() {
        if (!validators.isUnsatisfied()) {
            return validators.get();
        } else if (!validatorFactories.isUnsatisfied()) {
            ValidatorFactory validatorFactory = validatorFactories.get();
            return validatorFactory.getValidator();
        } else {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            return factory.getValidator();
        }
    }

}
