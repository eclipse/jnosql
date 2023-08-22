/*
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
 */
package org.eclipse.jnosql.mapping.document;

import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.PreparedStatement;
import org.eclipse.jnosql.communication.document.Document;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentEntity;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.document.entities.Job;
import org.eclipse.jnosql.mapping.document.entities.Person;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.jnosql.communication.document.DocumentDeleteQuery.delete;
import static org.eclipse.jnosql.communication.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
public class DefaultDocumentTemplateTest {

    private final Person person = Person.builder().
            withAge().
            withPhones(Arrays.asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore().build();

    private final Document[] documents = new Document[]{
            Document.of("age", 10),
            Document.of("phones", Arrays.asList("234", "432")),
            Document.of("name", "Name"),
            Document.of("id", 19L),
    };


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private DocumentManager managerMock;

    private DefaultDocumentTemplate template;

    private ArgumentCaptor<DocumentEntity> captor;

    private DocumentEventPersistManager documentEventPersistManager;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentManager.class);
        documentEventPersistManager = Mockito.mock(DocumentEventPersistManager.class);
        captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Instance<DocumentManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        this.template = new DefaultDocumentTemplate(converter, instance,
                documentEventPersistManager, entities, converters);
    }

    @Test
    public void shouldInsert() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(document);

        template.insert(this.person);
        verify(managerMock).insert(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.documents().size());
    }

    @Test
    public void shouldMergeOnInsert() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(document);

        Person person = Person.builder().build();
        Person result = template.insert(person);
        verify(managerMock).insert(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        DocumentEntity value = captor.getValue();
        assertSame(person, result);
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

        template.insert(this.person, twoHours);
        verify(managerMock).insert(captor.capture(), Mockito.eq(twoHours));
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.documents().size());
    }


    @Test
    public void shouldUpdate() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(document);

        template.update(this.person);
        verify(managerMock).update(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        DocumentEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.documents().size());
    }

    @Test
    public void shouldMergeOnUpdate() {
        DocumentEntity document = DocumentEntity.of("Person");
        document.addAll(Stream.of(documents).collect(Collectors.toList()));

        when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(document);

        Person person = Person.builder().build();
        Person result = template.update(person);
        verify(managerMock).update(captor.capture());
        verify(documentEventPersistManager).firePostEntity(any(Person.class));
        verify(documentEventPersistManager).firePreEntity(any(Person.class));
        DocumentEntity value = captor.getValue();
        assertSame(person, result);
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

        template.insert(Arrays.asList(person, person), duration);
        verify(managerMock, times(2)).insert(any(DocumentEntity.class), any(Duration.class));
    }

    @Test
    public void shouldInsertEntities() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(DocumentEntity.class)))
                .thenReturn(documentEntity);

        template.insert(Arrays.asList(person, person));
        verify(managerMock, times(2)).insert(any(DocumentEntity.class));
    }

    @Test
    public void shouldUpdateEntities() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(DocumentEntity.class)))
                .thenReturn(documentEntity);

        template.update(Arrays.asList(person, person));
        verify(managerMock, times(2)).update(any(DocumentEntity.class));
    }


    @Test
    public void shouldDelete() {

        DocumentDeleteQuery query = delete().from("delete").build();
        template.delete(query);
        verify(managerMock).delete(query);
    }

    @Test
    public void shouldSelect() {
        DocumentQuery query = select().from("Person").build();
        template.select(query);
        verify(managerMock).select(query);
    }

    @Test
    public void shouldCountBy() {
        DocumentQuery query = select().from("Person").where("age").gt(10).build();
        template.count(query);
        verify(managerMock).count(query);
    }

    @Test
    public void shouldGroupBy() {
        DocumentQuery query = select().from("Person").where("age").gt(10)
                .limit(1).build();
        template.count(query);
        verify(managerMock).count(query);
    }

    @Test
    public void shouldReturnSingleResult() {
        DocumentEntity documentEntity = DocumentEntity.of("Person");
        documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

        Mockito.when(managerMock
                .select(any(DocumentQuery.class)))
                .thenReturn(Stream.of(documentEntity));

        DocumentQuery query = select().from("person").build();

        Optional<Person> result = template.singleResult(query);
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnSingleResultIsEmpty() {
        Mockito.when(managerMock
                .select(any(DocumentQuery.class)))
                .thenReturn(Stream.empty());

        DocumentQuery query = select().from("person").build();

        Optional<Person> result = template.singleResult(query);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenThereMoreThanASingleResult() {
        Assertions.assertThrows(NonUniqueResultException.class, () -> {
            DocumentEntity documentEntity = DocumentEntity.of("Person");
            documentEntity.addAll(Stream.of(documents).collect(Collectors.toList()));

            Mockito.when(managerMock
                    .select(any(DocumentQuery.class)))
                    .thenReturn(Stream.of(documentEntity, documentEntity));

            DocumentQuery query = select().from("person").build();

            template.singleResult(query);
        });
    }

    @Test
    public void shouldReturnErrorWhenFindIdHasIdNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.find(Person.class, null));
    }

    @Test
    public void shouldReturnErrorWhenFindIdHasClassNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.find(null, "10"));
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotIdInFind() {
        Assertions.assertThrows(IdNotFoundException.class, () -> template.find(Job.class, "10"));
    }

    @Test
    public void shouldReturnFind() {
        template.find(Person.class, "10");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        DocumentCondition condition = query.condition().get();

        assertEquals("Person", query.name());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), condition);

    }

    @Test
    public void shouldDeleteEntity() {
        template.delete(Person.class, "10");
        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture());
        DocumentDeleteQuery query = queryCaptor.getValue();
        DocumentCondition condition = query.condition().get();

        assertEquals("Person", query.name());
        assertEquals(DocumentCondition.eq(Document.of("_id", 10L)), condition);

    }


    @Test
    public void shouldExecuteQuery() {
        template.query("select * from Person");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    public void shouldConvertEntity() {
        template.query("select * from Movie");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("movie", query.name());
    }


    @Test
    public void shouldConvertEntityName() {
        template.query("select * from download");
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("download", query.name());
    }
    @Test
    public void shouldConvertEntityNameClassName() {
        template.query("select * from " + Person.class.getName());
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    public void shouldConvertConvertFromAnnotationEntity(){
        template.query("select * from Vendor" );
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("vendors", query.name());
    }

    @Test
    public void shouldPreparedStatement() {
        PreparedStatement preparedStatement = template.prepare("select * from Person where name = @name");
        preparedStatement.bind("name", "Ada");
        preparedStatement.result();
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    public void shouldCount() {
        template.count("Person");
        verify(managerMock).count("Person");
    }

    @Test
    public void shouldCountFromEntityClass() {
        template.count(Person.class);
        verify(managerMock).count("Person");
    }

    @Test
    public void shouldFindAll(){
        template.findAll(Person.class);
        verify(managerMock).select(select().from("Person").build());
    }

    @Test
    public void shouldDeleteAll(){
        template.deleteAll(Person.class);
        verify(managerMock).delete(delete().from("Person").build());
    }


}
