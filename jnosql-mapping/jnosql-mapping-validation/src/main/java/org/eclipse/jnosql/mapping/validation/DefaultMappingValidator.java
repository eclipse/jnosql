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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
class DefaultMappingValidator implements MappingValidator {

    @Inject
    private Instance<ValidatorFactory> validatorFactories;

    @Inject
    private Instance<Validator> validators;

    public void validate(Object entity) {
        Objects.requireNonNull(entity, "entity is required");
        Validator validator = getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

    }

    @Override
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
