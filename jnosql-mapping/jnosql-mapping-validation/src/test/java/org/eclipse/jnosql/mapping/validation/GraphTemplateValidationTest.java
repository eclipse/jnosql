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

import jakarta.inject.Inject;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.graph.spi.GraphExtension;
import org.eclipse.jnosql.mapping.keyvalue.KeyValueWorkflow;
import org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@EnableAutoWeld
@AddPackages(value = {Convert.class, GraphTemplate.class, EntityObserver.class})
@AddPackages(Person.class)
@AddExtensions({EntityMetadataExtension.class, GraphExtension.class})
public class GraphTemplateValidationTest {

    @Inject
    private GraphTemplate template;


    @Test
    public void shouldValidate() {

        Person person = Person.builder()
                .withAge(21)
                .withName("10")
                .withSalary(BigDecimal.ONE)
                .withPhones(singletonList("123131231"))
                .build();
        assertThat(template.insert(person))
                .isNotNull();
    }

    @Test
    public void shouldReturnValidationException() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> {
            Person person = Person.builder()
                    .withAge(10)
                    .withSalary(BigDecimal.ONE)
                    .withPhones(singletonList("123131231"))
                    .build();
            template.insert(person);
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
            template.insert(person);
        } catch (ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
            assertEquals(2, violations.size());
        }

    }
}
