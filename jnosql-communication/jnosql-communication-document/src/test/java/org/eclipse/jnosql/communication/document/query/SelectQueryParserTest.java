/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.Condition;
import jakarta.nosql.QueryException;
import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.TypeReference;
import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatement;
import jakarta.nosql.document.DocumentQuery;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static jakarta.nosql.document.DocumentCondition.eq;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectQueryParserTest {

    private final SelectQueryParser parser = new SelectQueryParser();

    private final DocumentCollectionManager documentCollection = Mockito.mock(DocumentCollectionManager.class);

    private final DocumentObserverParser observer = new DocumentObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertThat(documentQuery.getDocuments(), contains("name", "address"));
        assertTrue(documentQuery.getSorts().isEmpty());
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), Matchers.contains(Sort.of("name", SortType.ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name asc"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", SortType.ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc"})
    public void shouldReturnParserQuery5(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", SortType.DESC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc age asc"})
    public void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", SortType.DESC),
                Sort.of("age", SortType.ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery7(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 12L);
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God limit 12"})
    public void shouldReturnParserQuery8(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 12L, 0L);
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 10 limit 12"})
    public void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertTrue(documentQuery.getSorts().isEmpty());
        assertEquals(12L, documentQuery.getLimit());
        assertEquals(10L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = 10"})
    public void shouldReturnParserQuery10(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("age", 10L), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", 10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= -10.23"})
    public void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        DocumentObserverParser observer = new DocumentObserverParser() {
        };
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= -10.23"})
    public void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < -10.23"})
    public void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals(Document.of("age", Arrays.asList(10L, 30L)), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "diana"), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(Condition.EQUALS, condition.getCondition());
        Document document = condition.getDocument();
        List<Document> documents = document.get(new TypeReference<List<Document>>() {
        });
        assertThat(documents, containsInAnyOrder(Document.of("apollo", "Brother"),
                Document.of("Zeus", "Father")));
        assertEquals("siblings", document.getName());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery19(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(Value.of(12), document.getValue());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.IN, condition.getCondition());
        assertEquals("name", document.getName());
        List<String> values = document.get(new TypeReference<List<String>>() {
        });
        assertThat(values, containsInAnyOrder("Ada", "Apollo"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals("Ada", document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.NOT, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        DocumentCondition documentCondition = conditions.get(0);
        assertEquals(Condition.LIKE, documentCondition.getCondition());
        assertEquals(Document.of("name", "Ada"), documentCondition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20"})
    public void shouldReturnParserQuery23(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L))));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" or age = 20"})
    public void shouldReturnParserQuery24(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.OR, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertThat(conditions, contains(eq(Document.of("name", "Ada")),
                eq(Document.of("age", 20L))));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).getCondition());
        assertEquals(Condition.EQUALS, conditions.get(1).getCondition());
        assertEquals(Condition.OR, conditions.get(2).getCondition());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery26(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(Condition.AND, condition.getCondition());
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).getCondition());
        assertEquals(Condition.EQUALS, conditions.get(1).getCondition());
        assertEquals(Condition.OR, conditions.get(2).getCondition());
        assertEquals(Condition.EQUALS, conditions.get(3).getCondition());
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
        assertThrows(QueryException.class, prepare::getResult);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);

        DocumentPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        prepare.bind("age", 12);
        prepare.getResult();
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(Condition.EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    private void checkBaseQuery(DocumentQuery documentQuery, long limit, long skip) {
        assertTrue(documentQuery.getDocuments().isEmpty());
        assertTrue(documentQuery.getSorts().isEmpty());
        assertEquals(limit, documentQuery.getLimit());
        assertEquals(skip, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
    }
}
