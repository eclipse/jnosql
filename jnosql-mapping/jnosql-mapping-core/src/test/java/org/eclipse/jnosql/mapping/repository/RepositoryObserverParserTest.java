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
package org.eclipse.jnosql.mapping.repository;

import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.VetedConverter;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.test.entities.Address;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.Worker;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;

@EnableAutoWeld
@AddPackages(value = Converters.class)
@AddPackages(value = VetedConverter.class)
@AddPackages(value = Reflections.class)
@AddExtensions(EntityMetadataExtension.class)
class RepositoryObserverParserTest {

    @Inject
    private EntitiesMetadata entities;

    @Test
    public void shouldCreateInstance() {
        EntityMetadata metadata = entities.get(Person.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser).isNotNull();
    }

    @Test
    public void shouldFireEvent() {
        EntityMetadata metadata = entities.get(Person.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser)
                .extracting(RepositoryObserverParser::name)
                .isEqualTo(metadata.name());
    }


    @Test
    public void shouldKeepName() {
        EntityMetadata metadata = entities.get(Person.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser)
                .extracting(p -> p.field("name"))
                .isEqualTo("name");
    }

    @Test
    public void shouldReplaceName(){
        EntityMetadata metadata = entities.get(Worker.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser)
                .extracting(p -> p.field("salary"))
                .isEqualTo("money");
    }

    @Test
    public void shouldKeepWhenDoesNotFind() {
        EntityMetadata metadata = entities.get(Address.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser)
                .extracting(p -> p.field("not-found"))
                .isEqualTo("not-found");
    }

    @Test
    public void shouldConcatSmart() {
        EntityMetadata metadata = entities.get(Address.class);
        RepositoryObserverParser parser = RepositoryObserverParser.of(metadata);
        org.assertj.core.api.Assertions.assertThat(parser)
                .extracting(p -> p.field("zipCodePlusFour"))
                .isEqualTo("zipCode.plusFour");
    }

}