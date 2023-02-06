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


import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import org.eclipse.jnosql.mapping.reflection.ConstructorEvent;

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

    private Validator validator;

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
        ExecutableValidator executableValidator = validator.forExecutables();
        Set<? extends ConstraintViolation<?>> violations =
                executableValidator.validateConstructorParameters(event.getConstructor(),
                        event.getParams());

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }
    }

    /**
     * Defines the {@link Validator} that it's going to be used during the validation process
     *
     * @throws NullPointerException when cannot to get the validator from CDI
     */
    @PostConstruct
    void postConstruct() {
        var validator = ValidatorSupplier.get(validatorFactories, validators);
        Objects.requireNonNull(validator, "validator is required");
        this.validator = validator;
    }

    private static class ValidatorSupplier {

        /**
         * Supplies a {Validator} dynamically from CDI
         *
         * @param validatorFactories the validatorFactories instance provided by the CDI
         * @param validators         the validators instance provided by the CDI
         * @throws NullPointerException when the provided validatorFactories or the validators instances are null
         */
        private static Validator get(Instance<ValidatorFactory> validatorFactories, Instance<Validator> validators) {
            Objects.requireNonNull(validatorFactories, "validatorFactories is required");
            Objects.requireNonNull(validators, "validators is required");
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
}
