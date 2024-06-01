/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.inject.Inject;
import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.SelectQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.semistructured.entities.Person;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

@EnableAutoWeld
@AddPackages(value = {Converters.class, EntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class})
class PreparedStatementTest {


    @Inject
    private EntitiesMetadata entitiesMetadata;

    @Inject
    private EntityConverter converter;

    @Test
    void shouldReturnCount(){
        var communicationPreparedStatement = Mockito.mock(org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement.class);
        Mockito.when(communicationPreparedStatement.count()).thenReturn(10L);
        var preparedStatement = new PreparedStatement(communicationPreparedStatement, converter);
        Assertions.assertThat(preparedStatement.count()).isEqualTo(10L);
    }

    @Test
    void shouldReturnFalseWhenQueryIsEmpty(){
        var communicationPreparedStatement = Mockito.mock(org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement.class);
        Mockito.when(communicationPreparedStatement.select()).thenReturn(Optional.empty());
        var preparedStatement = new PreparedStatement(communicationPreparedStatement, converter);
        Assertions.assertThat(preparedStatement.isCount()).isFalse();
    }

    @Test
    void shouldReturnCheckIsCountBaseOnQuery(){
        var communicationPreparedStatement = Mockito.mock(org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement.class);
        var query = Mockito.mock(SelectQuery.class);
        Mockito.when(query.isCount()).thenReturn(true);
        Mockito.when(communicationPreparedStatement.select()).thenReturn(Optional.of(query));
        var preparedStatement = new PreparedStatement(communicationPreparedStatement, converter);
        Assertions.assertThat(preparedStatement.isCount()).isTrue();
    }

    @Test
    void shouldReturnSingleResult(){
        var communicationPreparedStatement = Mockito.mock(org.eclipse.jnosql.communication.semistructured.CommunicationPreparedStatement.class);
        CommunicationEntity entity = CommunicationEntity.of("Person");
        entity.add("name", "Ada");
        entity.add("age", 20);
        entity.add("_id", 20);

        Mockito.when(communicationPreparedStatement.singleResult()).thenReturn(Optional.of(entity));

        var preparedStatement = new PreparedStatement(communicationPreparedStatement, converter);
        Optional<Person> person = preparedStatement.singleResult();

        Assertions.assertThat(person).isPresent();

    }

}