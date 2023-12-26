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
package org.eclipse.jnosql.mapping.core.query;

import jakarta.inject.Inject;
import jakarta.nosql.Template;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.core.VetedConverter;
import org.eclipse.jnosql.mapping.core.entities.Person;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@EnableAutoWeld
@AddPackages(value = Convert.class)
@AddPackages(value = VetedConverter.class)
@AddExtensions(org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension.class)
class AbstractRepositoryTest {

    private Template template;

    @Inject
    private EntitiesMetadata entitiesMetadata;

    private PeopleRepository repository;

    @Test
    void shouldInsert() {
        this.template = Mockito.mock(Template.class);
        Person person = Person.builder().withAge(10).withName("Ada").build();
        this.repository.insert(person);
        Mockito.verify(template).insert(person);
    }

    class PeopleRepository extends AbstractRepository<Person, Long> {

        @Override
        protected Template template() {
            return template;
        }

        @Override
        protected EntityMetadata entityMetadata() {
            return entitiesMetadata.get(Person.class);
        }
    }


}