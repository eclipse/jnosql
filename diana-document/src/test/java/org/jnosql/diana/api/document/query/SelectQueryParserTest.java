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
package org.jnosql.diana.api.document.query;

import org.hamcrest.Matchers;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Condition.BETWEEN;
import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.GREATER_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.LIKE;
import static org.jnosql.diana.api.Condition.NOT;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectQueryParserTest {

    private SelectQueryParser parser = new SelectQueryParser();

    private DocumentCollectionManager documentCollection = Mockito.mock(DocumentCollectionManager.class);


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
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
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name asc"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc"})
    public void shouldReturnParserQuery5(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", DESC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc age asc"})
    public void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertThat(documentQuery.getSorts(), contains(Sort.of("name", DESC), Sort.of("age", ASC)));
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery7(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 12L);
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God limit 12"})
    public void shouldReturnParserQuery8(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 12L, 0L);
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 10 limit 12"})
    public void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
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
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("age", 10L), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(GREATER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", 10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= -10.23"})
    public void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= -10.23"})
    public void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < -10.23"})
    public void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(LESSER_THAN, condition.getCondition());
        assertEquals(Document.of("stamina", -10.23), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(BETWEEN, condition.getCondition());
        assertEquals(Document.of("age", Arrays.asList(10L, 30L)), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "diana"), condition.getDocument());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();

        assertEquals(EQUALS, condition.getCondition());
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
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(EQUALS, condition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(Value.of(12), document.getValue());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(IN, condition.getCondition());
        assertEquals("name", document.getName());
        List<String> values = document.get(new TypeReference<List<String>>() {
        });
        assertThat(values, containsInAnyOrder("Ada", "Apollo"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(LIKE, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals("Ada", document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
        DocumentQuery documentQuery = captor.getValue();

        checkBaseQuery(documentQuery, 0L, 0L);
        assertTrue(documentQuery.getCondition().isPresent());
        DocumentCondition condition = documentQuery.getCondition().get();
        Document document = condition.getDocument();
        assertEquals(NOT, condition.getCondition());
    }

    private void checkBaseQuery(DocumentQuery documentQuery, long limit, long skip) {
        assertTrue(documentQuery.getDocuments().isEmpty());
        assertTrue(documentQuery.getSorts().isEmpty());
        assertEquals(limit, documentQuery.getLimit());
        assertEquals(skip, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
    }
}
