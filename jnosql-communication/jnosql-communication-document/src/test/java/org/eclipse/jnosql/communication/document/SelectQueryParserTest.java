/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.Sort;
import org.eclipse.jnosql.communication.SortType;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.document.DocumentCondition.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectQueryParserTest {

    private final SelectQueryParser parser = new SelectQueryParser();

    private final DocumentManager documentCollection = Mockito.mock(DocumentManager.class);

    private final DocumentObserverParser observer = new DocumentObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertThat(documentQuery.documents()).contains("name", "address");
        assertTrue(documentQuery.sorts().isEmpty());
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.documents().isEmpty());
        assertThat(documentQuery.sorts()).contains(Sort.of("name", SortType.ASC));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name asc"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.documents().isEmpty());
        assertThat(documentQuery.sorts()).contains(Sort.of("name", SortType.ASC));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc"})
    public void shouldReturnParserQuery5(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.documents().isEmpty());
        assertThat(documentQuery.sorts()).contains(Sort.of("name", SortType.DESC));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc age asc"})
    public void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.documents().isEmpty());
        assertThat(documentQuery.sorts()).contains(Sort.of("name", SortType.DESC),
                Sort.of("age", SortType.ASC));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery7(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 12L);
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God limit 12"})
    public void shouldReturnParserQuery8(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 12L, 0L);
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 10 limit 12"})
    public void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.documents().isEmpty());
        assertTrue(documentQuery.sorts().isEmpty());
        assertEquals(12L, documentQuery.getLimit());
        assertEquals(10L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = 10"})
    public void shouldReturnParserQuery10(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(Document.of("age", 10L), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals(Document.of("stamina", 10.23), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= -10.23"})
    public void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals(Document.of("stamina", -10.23), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= -10.23"})
    public void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Document.of("stamina", -10.23), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < -10.23"})
    public void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals(Document.of("stamina", -10.23), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals(Document.of("age", Arrays.asList(10L, 30L)), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(Document.of("name", "diana"), condition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        Document document = condition.document();
        List<Document> documents = document.get(new TypeReference<>() {
        });
        Assertions.assertThat(documents).contains(Document.of("apollo", "Brother"),
                Document.of("Zeus", "Father"));
        assertEquals("siblings", document.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery19(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", document.name());
        assertEquals(Value.of(12), document.value());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.IN, condition.condition());
        assertEquals("name", document.name());
        List<String> values = document.get(new TypeReference<>() {
        });
        assertThat(values).contains("Ada", "Apollo");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", document.name());
        assertEquals("Ada", document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.NOT, condition.condition());
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        DocumentCondition documentCondition = conditions.get(0);
        assertEquals(Condition.LIKE, documentCondition.condition());
        assertEquals(Document.of("name", "Ada"), documentCondition.document());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20"})
    public void shouldReturnParserQuery23(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.AND, condition.condition());
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" or age = 20"})
    public void shouldReturnParserQuery24(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.OR, condition.condition());
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {

        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.AND, condition.condition());
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery26(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.condition().isPresent());
        DocumentCondition condition = documentQuery.condition().get();
        Document document = condition.document();
        assertEquals(Condition.AND, condition.condition());
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());
        assertEquals(Condition.EQUALS, conditions.get(3).condition());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorWhenIsQueryWithParam(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, documentCollection, observer));


    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorWhenDontBindParameters(String query) {

        DocumentPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        assertThrows(QueryException.class, prepare::result);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);

        DocumentPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        prepare.bind("age", 12);
        prepare.result();
        Mockito.verify(documentCollection).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.condition().get();
        Document document = documentCondition.document();
        assertEquals(Condition.EQUALS, documentCondition.condition());
        assertEquals("age", document.name());
        assertEquals(12, document.get());
    }

    private void checkBaseQuery(DefaultDocumentQuery documentQuery, long limit, long skip) {
        assertTrue(documentQuery.documents().isEmpty());
        assertTrue(documentQuery.sorts().isEmpty());
        assertEquals(limit, documentQuery.getLimit());
        assertEquals(skip, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
    }
}
