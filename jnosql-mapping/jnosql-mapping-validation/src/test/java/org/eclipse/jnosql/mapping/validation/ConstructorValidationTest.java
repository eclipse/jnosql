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

import jakarta.data.exceptions.MappingException;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.reflection.ConstructorEvent;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Set;

@EnableAutoWeld
@AddPackages(value = {Convert.class})
@AddPackages(Person.class)
@AddExtensions({EntityMetadataExtension.class})
public class ConstructorValidationTest {

    @Inject
    private MappingValidator validator;

    @Test
    public void shouldReturnErrorWhenInvalidConstructor() {
        Constructor<Computer> constructor = getConstructor();
        Object[] params = new Object[]{null, 2000, ""};
        ConstructorEvent event = ConstructorEvent.of(constructor, params);
        ConstraintViolationException exception = Assertions.assertThrows(ConstraintViolationException.class,
                () -> validator.validate(event));

        Set<ConstraintViolation<?>> violations = exception.getConstraintViolations();
        Assertions.assertEquals(3, violations.size());
    }

    @Test
    public void shouldValidateByConstructor() {
        Constructor<Computer> constructor = getConstructor();
        Object[] params = new Object[]{"Computer", 2023, "Nice"};
        ConstructorEvent event = ConstructorEvent.of(constructor, params);
        validator.validate(event);

    }

    private Constructor<Computer> getConstructor() {
        try {
            return Computer.class.getConstructor(String.class,int.class, String.class);
        } catch (NoSuchMethodException error) {
            throw new MappingException("There is an error to find Computer constructor",error);
        }
    }
}
