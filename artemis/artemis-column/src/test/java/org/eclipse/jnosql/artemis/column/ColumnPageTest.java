/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.column;

import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import org.eclipse.jnosql.artemis.reflection.ClassMappings;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static jakarta.nosql.column.ColumnQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CDIExtension
class ColumnPageTest {

    @Inject
    private ColumnEntityConverter converter;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private ColumnFamilyManager managerMock;

    private DefaultColumnTemplate subject;

    private ArgumentCaptor<ColumnEntity> captor;

    private ColumnEventPersistManager columnEventPersistManager;


    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(ColumnFamilyManager.class);
        columnEventPersistManager = Mockito.mock(ColumnEventPersistManager.class);
        captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Instance<ColumnFamilyManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(managerMock);
        this.subject = new DefaultColumnTemplate(converter, instance, new DefaultColumnWorkflow(columnEventPersistManager, converter),
                columnEventPersistManager, classMappings, converters);

        Pagination pagination = Pagination.page(1).size(1);
        ColumnQueryPagination query = ColumnQueryPagination.of(select().from("person").build(), pagination);

        for (int index = 0; index <= 10; index++) {

            Column[] columns = new Column[]{
                    Column.of("age", index),
                    Column.of("name", "Ada " + index),
                    Column.of("_id", (long) index)};
            ColumnEntity columnEntity = ColumnEntity.of("Person");
            columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));
            when(managerMock.select(query)).thenReturn(Stream.of(columnEntity));
            query = query.next();
        }
    }

    @Test
    public void shouldExecuteQueryPagination() {

        Pagination pagination = Pagination.page(1).size(2);
        ColumnQueryPagination query = ColumnQueryPagination.of(select().from("person").build(), pagination);
        subject.select(query);
        verify(managerMock).select(query);
    }

    @Test
    public void shouldExecutePagination() {
        Pagination pagination = Pagination.page(1).size(1);
        ColumnQueryPagination query = ColumnQueryPagination.of(select().from("person").build(), pagination);
        Page<Person> page = subject.select(query);

        verify(managerMock).select(query);

        assertNotNull(page);
        assertEquals(pagination, page.getPagination());
    }

    @Test
    public void shouldReturnNPEWhenCollectionFactoryIsNull() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);
        assertThrows(NullPointerException.class, () -> page.getContent(null));
    }

    @Test
    public void shouldGetContent() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);

        List<Person> people = page.getContent().collect(Collectors.toList());
        assertEquals(1, people.size());
        assertEquals(0L, people.get(0).getId());
    }

    @Test
    public void shouldGetAsStream() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);

        Stream<Person> stream = page.get();
        assertNotNull(stream);
        assertEquals(1L, stream.count());
    }

    @Test
    public void shouldCreateCollectionFromCollectionFactory() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);

        ArrayList<Person> people = page.getContent(ArrayList::new);
        assertEquals(1, people.size());
    }

    @Test
    public void shouldRequestPageTwice() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);

        List<Person> people = page.getContent().collect(Collectors.toList());
        assertEquals(1, people.size());
        assertEquals(0L, people.get(0).getId());
        assertNotNull(page.getContent(ArrayList::new));
        assertNotNull(page.getContent(HashSet::new));
    }

    @Test
    public void shouldCreatePagination() {
        Pagination pagination = Pagination.page(1).size(1);
        Page<Person> page = createPage(pagination);

        assertEquals(0L, page.get().map(Person::getId).findFirst().orElse(-0L));

        Page<Person> nextPage = page.next();
        assertEquals(pagination.next(), nextPage.getPagination());
        assertEquals(1L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(2L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(3L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(4L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(5L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(6L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));

        nextPage = nextPage.next();
        assertEquals(7L, nextPage.get().map(Person::getId).findFirst().orElse(-0L));
    }

    @Test
    public void shouldExecutePaginationAsQuery() {
        Pagination pagination = Pagination.page(1).size(1);
        ColumnQueryPagination queryPagination = ColumnQueryPagination.of(select().from("person").build(), pagination);
        ColumnQuery query = queryPagination;
        List<Person> people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(0L, people.stream().map(Person::getId).findFirst().orElse(-0L));

        queryPagination = queryPagination.next();
        query = queryPagination;
        people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(1L, people.stream().map(Person::getId).findFirst().orElse(-0L));


        queryPagination = queryPagination.next();
        query = queryPagination;
        people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(2L, people.stream().map(Person::getId).findFirst().orElse(-0L));

        queryPagination = queryPagination.next();
        query = queryPagination;
        people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(3L, people.stream().map(Person::getId).findFirst().orElse(-0L));

        queryPagination = queryPagination.next();
        query = queryPagination;
        people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(4L, people.stream().map(Person::getId).findFirst().orElse(-0L));

        queryPagination = queryPagination.next();
        query = queryPagination;
        people = subject.<Person>select(query).collect(Collectors.toList());
        assertEquals(5L, people.stream().map(Person::getId).findFirst().orElse(-0L));
    }

    private Page<Person> createPage(Pagination pagination) {
        ColumnQueryPagination query = ColumnQueryPagination.of(select().from("person").build(), pagination);
        return subject.select(query);
    }


}