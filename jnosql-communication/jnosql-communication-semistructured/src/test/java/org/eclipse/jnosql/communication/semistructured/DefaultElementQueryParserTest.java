/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import org.eclipse.jnosql.communication.Condition;
import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.communication.QueryException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultElementQueryParserTest {
    
    private final QueryParser parser = new QueryParser();


    private final DatabaseManager manager = Mockito.mock(DatabaseManager.class);

    @Test
    void shouldReturnNPEWhenThereIsNullParameter() {
        assertThrows(NullPointerException.class, () -> parser.query(null, manager, CommunicationObserverParser.EMPTY));
        assertThrows(NullPointerException.class, () -> parser.query("select * from God", null, CommunicationObserverParser.EMPTY));
    }

    @Test
    void shouldReturnErrorWhenHasInvalidQuery() {
        assertThrows(QueryException.class, () -> parser.query("inva", manager, CommunicationObserverParser.EMPTY));
        assertThrows(QueryException.class, () -> parser.query("invalid", manager, CommunicationObserverParser.EMPTY));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    void shouldReturnParserQuery(String query) {
        ArgumentCaptor<SelectQuery> captor = ArgumentCaptor.forClass(SelectQuery.class);
        parser.query(query, manager, CommunicationObserverParser.EMPTY);
        Mockito.verify(manager).select(captor.capture());
        SelectQuery selectQuery = captor.getValue();

        assertTrue(selectQuery.columns().isEmpty());
        assertTrue(selectQuery.sorts().isEmpty());
        assertEquals(0L, selectQuery.limit());
        assertEquals(0L, selectQuery.skip());
        assertEquals("God", selectQuery.name());
        assertFalse(selectQuery.condition().isPresent());

    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, CommunicationObserverParser.EMPTY);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery deleteQuery = captor.getValue();

        assertTrue(deleteQuery.columns().isEmpty());
        assertEquals("God", deleteQuery.name());
        assertFalse(deleteQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        parser.query(query, manager, CommunicationObserverParser.EMPTY);
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();


        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        parser.query(query, manager, CommunicationObserverParser.EMPTY);
        Mockito.verify(manager).update(captor.capture());
        CommunicationEntity entity = captor.getValue();


        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.result();
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery deleteQuery = captor.getValue();
        CriteriaCondition criteriaCondition = deleteQuery.condition().get();
        Element element = criteriaCondition.element();
        assertEquals(Condition.EQUALS, criteriaCondition.condition());
        assertEquals("age", element.name());
        assertEquals(12, element.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    void shouldExecutePrepareStatement1(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.result();
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();
        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    void shouldExecutePrepareStatement2(String query) {
        ArgumentCaptor<SelectQuery> captor = ArgumentCaptor.forClass(SelectQuery.class);

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.result();
        Mockito.verify(manager).select(captor.capture());
        SelectQuery selectQuery = captor.getValue();
        CriteriaCondition criteriaCondition = selectQuery.condition().get();
        Element element = criteriaCondition.element();
        assertEquals(Condition.EQUALS, criteriaCondition.condition());
        assertEquals("age", element.name());
        assertEquals(12, element.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    void shouldExecutePrepareStatement3(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.result();
        Mockito.verify(manager).update(captor.capture());
        CommunicationEntity entity = captor.getValue();
        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    void shouldSingleResult(String query) {
        ArgumentCaptor<SelectQuery> captor = ArgumentCaptor.forClass(SelectQuery.class);

        Mockito.when(manager.select(Mockito.any(SelectQuery.class)))
                .thenReturn(Stream.of(Mockito.mock(CommunicationEntity.class)));

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<CommunicationEntity> result = prepare.singleResult();
        Mockito.verify(manager).select(captor.capture());
        SelectQuery selectQuery = captor.getValue();
        CriteriaCondition criteriaCondition = selectQuery.condition().get();
        Element element = criteriaCondition.element();
        assertEquals(Condition.EQUALS, criteriaCondition.condition());
        assertEquals("age", element.name());
        assertEquals(12, element.get());
        assertTrue(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    void shouldReturnEmptySingleResult(String query) {
        ArgumentCaptor<SelectQuery> captor = ArgumentCaptor.forClass(SelectQuery.class);

        Mockito.when(manager.select(Mockito.any(SelectQuery.class)))
                .thenReturn(Stream.empty());

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<CommunicationEntity> result = prepare.singleResult();
        Mockito.verify(manager).select(captor.capture());
        SelectQuery selectQuery = captor.getValue();
        CriteriaCondition criteriaCondition = selectQuery.condition().get();
        Element element = criteriaCondition.element();
        assertEquals(Condition.EQUALS, criteriaCondition.condition());
        assertEquals("age", element.name());
        assertEquals(12, element.get());
        assertFalse(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    void shouldReturnErrorSingleResult(String query) {
        ArgumentCaptor<SelectQuery> captor = ArgumentCaptor.forClass(SelectQuery.class);

        Mockito.when(manager.select(Mockito.any(SelectQuery.class)))
                .thenReturn(Stream.of(Mockito.mock(CommunicationEntity.class), Mockito.mock(CommunicationEntity.class)));

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, CommunicationObserverParser.EMPTY);
        prepare.bind("age", 12);
       assertThrows(NonUniqueResultException.class, prepare::singleResult);
    }

}
