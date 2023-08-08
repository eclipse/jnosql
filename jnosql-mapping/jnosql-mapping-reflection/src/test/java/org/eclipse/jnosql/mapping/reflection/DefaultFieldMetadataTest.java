/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.reflection;

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.test.entities.MoneyConverter;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)

class DefaultFieldMetadataTest {

    @Inject
    private ClassConverter converter;

    private DefaultFieldMetadata fieldMetadata;

    @BeforeEach
    public void setUp(){
        EntityMetadata entityMetadata = converter.create(Person.class);
        FieldMetadata name = entityMetadata.fieldMapping("name").orElseThrow();
        this.fieldMetadata = (DefaultFieldMetadata) name;
    }
    @Test
    public void shouldToString() {
        Assertions.assertThat(fieldMetadata.toString()).isNotEmpty().isNotNull();
    }


    @Test
    public void shouldCreateNewInstanceConverter(){
        EntityMetadata entityMetadata = converter.create(Worker.class);
        FieldMetadata name = entityMetadata.fieldMapping("salary").orElseThrow();
        fieldMetadata = (DefaultFieldMetadata) name;
        AttributeConverter<Object, Object> result = fieldMetadata.newConverter().orElseThrow();
        Assertions.assertThat(result).isNotNull().isInstanceOf(MoneyConverter.class);
    }
}