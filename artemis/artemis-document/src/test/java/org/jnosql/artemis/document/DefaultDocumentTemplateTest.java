/*
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
 */
package org.jnosql.artemis.document;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.model.Job;
import org.jnosql.artemis.model.Movie;
import org.jnosql.artemis.model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.nosql.document.DocumentDeleteQuery.delete;
import static jakarta.nosql.document.DocumentQuery.select;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(CDIExtension.class)
public class DefaultDocumentTemplateTest {

    private Person person = Person.builder().
            withAge().
            withPhones(Arrays.asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore().build();

    private Document[] documents = new Document[]{
            Document.of("age", 10),
            Document.of("phones", Arrays.asList("234", "432")),
            Document.of("name", "Name"),
            Document.of("id", 19L),
    };


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private DocumentCollectionManager managerMock;

    private DefaultDocumentTemplate subject;

    private ArgumentCaptor<DocumentEntity> captor;

    private DocumentEventPersistManager documentEventPersistManager;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentCollectionManager.class);
        documentEventPersistManager = Mockito.mock(DocumentEventPersistManager.class);
        captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Instance<DocumentCollectionManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        DefaultDocumentWorkflow workflow = new DefaultDocumentWorkflow(documentEventPersistManager, converter);
        this.subject = new DefaultDocumentTemplate(converter, instance, workflow,
                documentEventPersistManager, classMappings, converters);
    }

    @Test
    public void shouldInsert() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(document);

        subject.insert(this.person);
        verify(managerMock).insert(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        verify(documentEventPersistManager).firePreDocument(any(DocumentEntity.class));
        verify(documentEventPersistManager).firePostDocument(any(DocumentEntity.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

    @Test
    public void shouldMergeOnInsert() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(document);

        Person person = Person.builder().build();
        Person result = subject.insert(person);
        verify(managerMock).insert(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        verify(documentEventPersistManager).firePreDocument(any(DocumentEntity.class));
        verify(documentEventPersistManager).firePostDocument(any(DocumentEntity.class));
        DocumentEntity value = captor.getValue();
        assertTrue(person == result);
        assertEquals(10, person.getAge());
    }


    @Test
    public void shouldSaveTTL() {

        Duration twoHours = Duration.ofHours(2L);

        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock.insert(any(DocumentEntity.class),
                Mockito.eq(twoHours)))
                .thenReturn(document);

        subject.insert(this.person, twoHours);
        verify(managerMock).insert(captor.capture(), Mockito.eq(twoHours));
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        verify(documentEventPersistManager).firePreDocument(any(DocumentEntity.class));
        verify(documentEventPersistManager).firePostDocument(any(DocumentEntity.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }


    @Test
    public void shouldUpdate() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(document);

        subject.update(this.person);
        verify(managerMock).update(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        verify(documentEventPersistManager).firePreDocument(any(DocumentEntity.class));
        verify(documentEventPersistManager).firePostDocument(any(DocumentEntity.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

    @Test
    public void shouldMergeOnUpdate() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(document);

        Person person = Person.builder().build();
        Person result = subject.update(person);
        verify(managerMock).update(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        verify(documentEventPersistManager).firePreDocument(any(DocumentEntity.class));
        verify(documentEventPersistManager).firePostDocument(any(DocumentEntity.class));
        DocumentEntity value = captor.getValue();
        assertTrue(person == result);
        assertEquals(10, person.getAge());
    }




    @Test
    public void shouldInsertEntitiesTTL() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));
        Duration duration = Duration.ofHours(2);

        Mockito.when(managerMock
                .insert(any(DocumentEntity.class), Mockito.eq(duration)))
                .thenReturn(documentEntity);

        subject.insert(Arrays.asList(person, person), duration);
        verify(managerMock, times(2)).insert(any(DocumentEntity.class), any(Duration.class));
    }

    @Test
    public void shouldInsertEntities() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(documentEntity);

        subject.insert(Arrays.asList(person, person));
        verify(managerMock, times(2)).insert(any(DocumentEntity.class));
    }

    @Test
    public void shouldUpdateEntities() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(documentEntity);

        subject.update(Arrays.asList(person, person));
        verify(managerMock, times(2)).update(any(DocumentEntity.class));
    }


    @Test
    public void shouldDelete() {

        DocumentDeleteQuery query = delete().from("delete").build();
        subject.delete(query);
        verify(managerMock).delete(query);
    }

    @Test
    public void shouldSelect() {
        DocumentQuery query = select().from("delete").build();
        subject.select(query);
        verify(managerMock).select(query);
    }


    @Test
    public void shouldReturnSingleResult() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .select(any(DocumentQuery.class)))
                .thenReturn(singletonList(documentEntity));

        DocumentQuery query = select().from("person").build();

        Optional<Person> result = subject.singleResult(query);
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnSingleResultIsEmpty() {
        Mockito.when(managerMock
                .select(any(DocumentQuery.class)))
                .thenReturn(emptyList());

        DocumentQuery query = select().from("person").build();

        Optional<Person> result = subject.singleResult(query);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenThereMoreThanASingleResult() {
        Assertions.assertThrows(NonUniqueResultException.class, () -> {
            DocumentEntity documentEntity = DocumentEntity.of("Person");
            documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

            Mockito.when(managerMock
                    .select(any(DocumentQuery.class)))
                    .thenReturn(Arrays.asList(documentEntity, documentEntity));

            DocumentQuery query = select().from("person").build();

            subject.singleResult(query);
        });
    }

    @Test
    public void shouldReturnErrorWhenFindIdHasIdNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.find(Person.class, null));
    }

    @Test
    public void shouldReturnErrorWhenFindIdHasClassNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.find(null, "10"));
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotIdInFind() {
        Assertions.assertThrows(IdNotFoundException.class, () -> subject.find(Job.class, "10"));
    }

    @Test
    public void shouldReturnFind() {
        subject.find(Person.class, "10");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        DocumentCondition condition = query.getCondition().get();

        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), condition);

    }

    @Test
    public void shouldDeleteEntity() {
        subject.delete(Person.class, "10");
        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture());
        DocumentDeleteQuery query = queryCaptor.getValue();
        DocumentCondition condition = query.getCondition().get();

        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), condition);

    }

    @Test
    public void shouldExecuteQuery() {
        List<Person> people = subject.query("select * from Person");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
    }

    @Test
    public void shouldConvertEntity() {
        List<Movie> movies = subject.query("select * from Movie");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("movie", query.getDocumentCollection());
    }

    @Test
    public void shouldPreparedStatement() {
        PreparedStatement preparedStatement = subject.prepare("select * from Person where name = @name");
        preparedStatement.bind("name", "Ada");
        preparedStatement.getResultList();
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
    }

    @Test
    public void shouldCount() {
        subject.count("Person");
        verify(managerMock).count("Person");
    }

    @Test
    public void shouldCountFromEntityClass() {
        subject.count(Person.class);
        verify(managerMock).count("Person");
    }


}