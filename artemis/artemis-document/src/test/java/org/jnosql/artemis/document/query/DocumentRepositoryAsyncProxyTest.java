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
package org.jnosql.artemis.document.query;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.DynamicQueryException;
import org.jnosql.artemis.Param;
import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(CDIExtension.class)
public class DocumentRepositoryAsyncProxyTest {

    private DocumentTemplateAsync template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private PersonAsyncRepository personRepository;


    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(DocumentTemplateAsync.class);

        DocumentRepositoryAsyncProxy handler = new DocumentRepositoryAsyncProxy(template,
                classMappings, PersonAsyncRepository.class, converters);


        personRepository = (PersonAsyncRepository) Proxy.newProxyInstance(PersonAsyncRepository.class.getClassLoader(),
                new Class[]{PersonAsyncRepository.class},
                handler);
    }


    @Test
    public void shouldSaveUsingInsertWhenThereIsNotData() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();

        personRepository.save(person);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), consumerCaptor.capture());
        Consumer consumer = consumerCaptor.getValue();
        consumer.accept(Optional.empty());
        verify(template).insert(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }

    @Test
    public void shouldSaveUsingUpdateWhenThereIsData() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        personRepository.save(person);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), consumerCaptor.capture());
        Consumer consumer = consumerCaptor.getValue();
        consumer.accept(Optional.of(Person.builder().build()));
        verify(template).update(captor.capture());
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldSaveWithTTl() {
        ArgumentCaptor<Person> captor = ArgumentCaptor.forClass(Person.class);
        Person person = Person.builder().withName("Ada")
                .withId(10L)
                .withPhones(singletonList("123123"))
                .build();
        template.insert(person, Duration.ofHours(2));
        verify(template).insert(captor.capture(), ArgumentMatchers.eq(Duration.ofHours(2)));
        Person value = captor.getValue();
        assertEquals(person, value);
    }


    @Test
    public void shouldDeleteByName() {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        personRepository.deleteByName("name");
        verify(template).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());

    }

    @Test
    public void shouldDeleteByNameCallBack() {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        Consumer<Void> voidConsumer = v -> {
        };
        personRepository.deleteByName("name", voidConsumer);
        verify(template).delete(captor.capture(), consumerCaptor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());
        assertEquals(voidConsumer, consumerCaptor.getValue());
    }


    @Test
    public void shoudReturnErrorOnFindByName() {
        Assertions.assertThrows(DynamicQueryException.class, () -> personRepository.findByName("name"));
    }

    @Test
    public void shoudFindByName() {
        Consumer<List<Person>> callback = v -> {
        };

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        personRepository.findByName("name", callback);
        verify(template).select(captor.capture(), consumerCaptor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());
        assertEquals(callback, consumerCaptor.getValue());
    }


    @Test
    public void shouldFindByNameOrderByAgeDesc() {
        Consumer<List<Person>> callback = v -> {
        };

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        ArgumentCaptor<Consumer> consumerCaptor = ArgumentCaptor.forClass(Consumer.class);

        personRepository.findByNameOrderByAgeDesc("name", callback);
        verify(template).select(captor.capture(), consumerCaptor.capture());
        DocumentQuery query = captor.getValue();
        DocumentCondition condition = query.getCondition().get();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals(Document.of("name", "name"), condition.getDocument());
        assertEquals(callback, consumerCaptor.getValue());
        assertEquals(Sort.of("age", Sort.SortType.DESC), query.getSorts().get(0));

    }

    @Test
    public void shouldFindById() {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Consumer<Optional<Person>> callBack = p -> {
        };
        personRepository.findById(10L, callBack);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), ArgumentMatchers.eq(callBack));

    }


    @Test
    public void shouldDeleteById() {
        personRepository.deleteById(10L);
        verify(template).delete(Person.class, 10L);
    }


    @Test
    public void shouldExistsById() {
        Consumer<Boolean> callback = v -> {
        };
        personRepository.existsById(10L, callback);
        verify(template).find(Mockito.eq(Person.class), Mockito.eq(10L), any(Consumer.class));


    }

    @Test
    public void shouldReturnToString() {
        assertNotNull(personRepository.toString());
    }

    @Test
    public void shouldReturnHasCode() {
        assertNotNull(personRepository.hashCode());
        assertEquals(personRepository.hashCode(), personRepository.hashCode());
    }

    @Test
    public void shouldExecuteJNoSQLQuery() {
        personRepository.findByQuery();
        verify(template).query(Mockito.eq("select * from Person"), Mockito.any(Consumer.class));
    }

    @Test
    public void shouldExecuteJNoSQLPrepare() {
        PreparedStatementAsync statement = Mockito.mock(PreparedStatementAsync.class);
        when(template.prepare(Mockito.anyString())).thenReturn(statement);
        personRepository.findByQuery("Ada", l ->{});
        verify(statement).bind("id", "Ada");
    }

    @Test
    public void shouldReturnEquals() {
        assertNotNull(personRepository.equals(personRepository));
    }


    interface PersonAsyncRepository extends RepositoryAsync<Person, Long> {

        void deleteByName(String name);

        void deleteByName(String name, Consumer<Void> callback);

        void findByName(String name);

        void findByName(String name, Consumer<List<Person>> callBack);

        void findByNameOrderByAgeDesc(String name, Consumer<List<Person>> callBack);

        @Query("select * from Person")
        void findByQuery();

        @Query("select * from Person where id = @id")
        void findByQuery(@Param("id") String id, Consumer<List<Person>> calback);
    }

}