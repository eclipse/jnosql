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
package org.jnosql.artemis.validation;


import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.HashSet;
import java.util.Set;

class DefaultArtemisValidator implements ArtemisValidator {

    @Inject
    private Instance<ValidatorFactory> validatorFactories;

    @Inject
    private Instance<Validator> validators;

    public void validate(Object bean) {
        Validator validator = getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);

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
