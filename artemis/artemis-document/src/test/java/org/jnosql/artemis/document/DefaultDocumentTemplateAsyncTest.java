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

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.artemis.model.Movie;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.NonUniqueResultException;
import org.jnosql.diana.document.Document;
import org.jnosql.diana.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.document.DocumentCondition;
import org.jnosql.diana.document.DocumentDeleteQuery;
import org.jnosql.diana.document.DocumentEntity;
import org.jnosql.diana.document.DocumentQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.awaitility.Awaitility.await;
import static org.jnosql.diana.document.query.DocumentQueryBuilder.delete;
import static org.jnosql.diana.document.query.DocumentQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(CDIExtension.class)
public class DefaultDocumentTemplateAsyncTest {

    private Person person = Person.builder().
            withAge().
            withPhones(asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore().build();

    private Document[] documents = new Document[]{
            Document.of("age", 10),
            Document.of("phones", asList("234", "432")),
            Document.of("name", "Name"),
            Document.of("id", 19L),
    };


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private DocumentCollectionManagerAsync managerMock;

    private DefaultDocumentTemplateAsync subject;

    private ArgumentCaptor<DocumentEntity> captor;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentCollectionManagerAsync.class);
        DocumentEventPersistManager documentEventPersistManager = Mockito.mock(DocumentEventPersistManager.class);
        captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Instance<DocumentCollectionManagerAsync> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        this.subject = new DefaultDocumentTemplateAsync(converter, instance, classMappings, converters);
    }

    @Test
    public void shouldCheckNullParameterInInsert() {

        assertThrows(NullPointerException.class, () -> subject.insert(null));
        assertThrows(NullPointerException.class, () -> subject.insert(null));
        assertThrows(NullPointerException.class, () -> subject.insert(this.person, (Duration) null));
        assertThrows(NullPointerException.class, () -> subject.insert(null, Duration.ofSeconds(1L)));
        assertThrows(NullPointerException.class, () -> subject.insert(this.person, (Consumer<Person>) null));
        assertThrows(NullPointerException.class, () -> subject.insert(null, System.out::println));

    }


    @Test
    public void shouldInsert() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));


        subject.insert(this.person);
        verify(managerMock).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

    @Test
    public void shouldInsertTTL() {

        Duration twoHours = Duration.ofHours(2L);

        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));


        subject.insert(this.person, twoHours);
        verify(managerMock).insert(captor.capture(), Mockito.eq(twoHours), Mockito.any(Consumer.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

    @Test
    public void shouldInsertIterable() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));

        subject.insert(singletonList(this.person));
        verify(managerMock).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity value = captor.getValue();
        assertEquals(entity.getName(), value.getName());
    }

    @Test
    public void shouldInsertIterableTTL() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));

        subject.insert(singletonList(this.person), Duration.ofSeconds(1L));
        verify(managerMock).insert(Mockito.any(DocumentEntity.class), Mockito.eq(Duration.ofSeconds(1L)), Mockito.any(Consumer.class));
    }

    @Test
    public void shouldCheckNullParameterInUpdate() {
        assertThrows(NullPointerException.class, () -> subject.update(null));
        assertThrows(NullPointerException.class, () -> subject.update((Iterable) null));
        assertThrows(NullPointerException.class, () -> subject.update(singletonList(person), null));
        assertThrows(NullPointerException.class, () -> subject.update((Iterable) null, System.out::println));
    }

    @Test
    public void shouldUpdate() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));


        subject.update(this.person);
        verify(managerMock).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getDocuments().size());
    }

    @Test
    public void shouldUpdateIterable() {
        DocumentEntity entity = DocumentEntity.of("Person");
        entity.addAll(Stream.of(documents).collect(Collectors.toList()));

        subject.update(singletonList(this.person));
        verify(managerMock).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity value = captor.getValue();
        assertEquals(entity.getName(), value.getName());
    }

    @Test
    public void shouldCheckNullParameterInDelete() {
        assertThrows(NullPointerException.class, () -> subject.delete(null));
        assertThrows(NullPointerException.class, () -> subject.delete(delete().from("delete").build(), null));
        assertThrows(NullPointerException.class, () -> subject.delete(Person.class, null));
        assertThrows(NullPointerException.class, () -> subject.delete((Class) null, 10L));
        assertThrows(NullPointerException.class, () -> subject.delete(Person.class, 10L, null));
    }


    @Test
    public void shouldDelete() {

        DocumentDeleteQuery query = delete().from("delete").build();
        subject.delete(query);
        verify(managerMock).delete(query);
    }

    @Test
    public void shouldDeleteCallBack() {

        DocumentDeleteQuery query = delete().from("delete").build();
        Consumer<Void> callback = v -> {

        };
        subject.delete(query, callback);
        verify(managerMock).delete(query, callback);
    }

    @Test
    public void shouldDeleteByEntity() {
        subject.delete(Person.class, 10L);

        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture());

        DocumentDeleteQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), query.getCondition().get());

    }

    @Test
    public void shouldDeleteByEntityCallBack() {

        Consumer<Void> callback = v -> {
        };
        subject.delete(Person.class, 10L, callback);

        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture(), Mockito.eq(callback));

        DocumentDeleteQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), query.getCondition().get());

    }

    @Test
    public void shouldCheckNullParameterInSelect() {
        assertThrows(NullPointerException.class, () -> subject.select(null, null));
        assertThrows(NullPointerException.class, () -> subject.select(null, System.out::println));
        assertThrows(NullPointerException.class, () -> subject.select(select().from("Person").build(),
                null));
    }


    @Test
    public void shouldSelect() {

        ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        DocumentQuery query = select().from("Person").build();
        AtomicBoolean condition = new AtomicBoolean(false);
        Consumer<List<Person>> callback = l -> condition.set(true);
        subject.select(query, callback);
        verify(managerMock).select(Mockito.any(DocumentQuery.class), dianaCallbackCaptor.capture());
        Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
        dianaCallBack.accept(singletonList(DocumentEntity.of("Person", asList(documents))));
        verify(managerMock).select(Mockito.eq(query), Mockito.any());
        await().untilTrue(condition);
    }

    @Test
    public void shouldReturnSingleResult() {

        ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        DocumentQuery query = select().from("Person").build();
        AtomicBoolean condition = new AtomicBoolean(false);
        AtomicReference<Person> atomicReference = new AtomicReference<>();
        Consumer<Optional<Person>> callback = p -> {
            condition.set(true);
            p.ifPresent(atomicReference::set);
        };
        subject.singleResult(query, callback);
        verify(managerMock).select(Mockito.any(DocumentQuery.class), dianaCallbackCaptor.capture());
        Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
        dianaCallBack.accept(singletonList(DocumentEntity.of("Person", asList(documents))));
        verify(managerMock).select(Mockito.eq(query), Mockito.any());
        await().untilTrue(condition);
        assertNotNull(atomicReference.get());
    }

    @Test
    public void shouldReturnEmptySingleResult() {

        ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        DocumentQuery query = select().from("Person").build();
        AtomicBoolean condition = new AtomicBoolean(false);
        AtomicReference<Person> atomicReference = new AtomicReference<>();
        Consumer<Optional<Person>> callback = p -> {
            condition.set(true);
            p.ifPresent(atomicReference::set);
        };
        subject.singleResult(query, callback);
        verify(managerMock).select(Mockito.any(DocumentQuery.class), dianaCallbackCaptor.capture());
        Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
        dianaCallBack.accept(emptyList());
        verify(managerMock).select(Mockito.eq(query), Mockito.any());
        await().untilTrue(condition);
        assertNull(atomicReference.get());
    }

    @Test
    public void shouldReturnErrorWhenThereIsMoreThanOneResultInSingleResult() {

        assertThrows(NonUniqueResultException.class, () -> {
            ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);
            DocumentQuery query = select().from("Person").build();
            Consumer<Optional<Person>> callback = l -> {
            };
            subject.singleResult(query, callback);
            verify(managerMock).select(Mockito.any(DocumentQuery.class), dianaCallbackCaptor.capture());
            Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
            dianaCallBack.accept(asList(DocumentEntity.of("Person", asList(documents)),
                    DocumentEntity.of("Person", asList(documents))));

        });
    }

    @Test
    public void shouldCheckNullParameterInFindById() {
        assertThrows(NullPointerException.class, () -> subject.find(null, null, null));
        assertThrows(NullPointerException.class, () -> subject.find(Person.class, null, null));
        assertThrows(NullPointerException.class, () -> subject.find(Person.class, 10L, null));
        assertThrows(NullPointerException.class, () -> subject.find(Person.class, null, System.out::println));
        assertThrows(NullPointerException.class, () -> subject.find(null, null, System.out::println));
        assertThrows(NullPointerException.class, () -> subject.find(null, 10L, System.out::println));
        assertThrows(NullPointerException.class, () -> subject.find(null, 10L, null));
    }

    @Test
    public void shouldFindById() {
        ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);

        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        AtomicBoolean condition = new AtomicBoolean(false);
        AtomicReference<Person> atomicReference = new AtomicReference<>();
        Consumer<Optional<Person>> callback = p -> {
            condition.set(true);
            p.ifPresent(atomicReference::set);
        };

        subject.find(Person.class, 10L, callback);
        verify(managerMock).select(queryCaptor.capture(), dianaCallbackCaptor.capture());
        Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
        dianaCallBack.accept(singletonList(DocumentEntity.of("Person", asList(documents))));
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), query.getCondition().get());
        assertNotNull(atomicReference.get());

    }

    @Test
    public void shouldFindByIdReturnEmptyWhenElementDoesNotFind() {
        ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);

        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        AtomicBoolean condition = new AtomicBoolean(false);
        AtomicReference<Person> atomicReference = new AtomicReference<>();
        Consumer<Optional<Person>> callback = p -> {
            condition.set(true);
            p.ifPresent(atomicReference::set);
        };

        subject.find(Person.class, 10L, callback);
        verify(managerMock).select(queryCaptor.capture(), dianaCallbackCaptor.capture());
        Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
        dianaCallBack.accept(emptyList());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), query.getCondition().get());
        assertNull(atomicReference.get());

    }

    @Test
    public void shouldReturnErrorFindByIdReturnMoreThanOne() {

        assertThrows(NonUniqueResultException.class, () -> {
            ArgumentCaptor<Consumer<List<DocumentEntity>>> dianaCallbackCaptor = ArgumentCaptor.forClass(Consumer.class);

            ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
            AtomicBoolean condition = new AtomicBoolean(false);
            AtomicReference<Person> atomicReference = new AtomicReference<>();
            Consumer<Optional<Person>> callback = p -> {
                condition.set(true);
                p.ifPresent(atomicReference::set);
            };

            subject.find(Person.class, 10L, callback);
            verify(managerMock).select(queryCaptor.capture(), dianaCallbackCaptor.capture());
            Consumer<List<DocumentEntity>> dianaCallBack = dianaCallbackCaptor.getValue();
            dianaCallBack.accept(asList(DocumentEntity.of("Person", asList(documents)),
                    DocumentEntity.of("Person", asList(documents))));
        });

    }

    @Test
    public void shouldExecuteQuery() {
        Consumer<List<Person>> callback = l ->{};
        subject.query("select * from Person", callback);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture(), Mockito.any(Consumer.class));
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
    }

    @Test
    public void shouldConvertEntity() {
        Consumer<List<Movie>> callback = l ->{};
        subject.query("select * from Movie", callback);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture(), Mockito.any(Consumer.class));
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("movie", query.getDocumentCollection());
    }

    @Test
    public void shouldPreparedStatement() {
        Consumer<List<Person>> callback = l ->{};
        PreparedStatementAsync preparedStatement = subject.prepare("select * from Person where name = @name");
        preparedStatement.bind("name", "Ada");
        preparedStatement.getResultList(callback);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture(), Mockito.any(Consumer.class));
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getDocumentCollection());
    }

    @Test
    public void shouldCount() {
        Consumer<Long> callback = l ->{};
        subject.count("Person", callback);
        verify(managerMock).count("Person", callback);
    }

    @Test
    public void shouldCountFromEntityClass() {
        Consumer<Long> callback = l ->{};
        subject.count(Person.class, callback);
        verify(managerMock).count("Person", callback);
    }

}