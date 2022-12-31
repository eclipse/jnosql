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

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class KeyValueTemplateValidationTest {

    @Inject
    private KeyValueTemplate template;


    @Test
    public void shouldValidate() {

        Person person = Person.builder()
                .withAge(21)
                .withName("Ada")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        template.put(person);
    }

    @Test
    public void shouldReturnValidationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Person person = Person.builder()
                    .withAge(10)
                    .withName("Ada")
                    .withSalary(BigDecimal.ONE)
                    .withPhones(singletonList("123131231"))
                    .build();
            template.put(person);
        });
    }


    @Test
    public void shouldGetValidations() {

        Person person = Person.builder()
                .withAge(10)
                .withName("Ada")
                .withSalary(BigDecimal.valueOf(12991))
                .withPhones(singletonList("123131231"))
                .build();
        try {
            template.put(person);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            assertEquals(2, violations.size());
        }

    }
}
