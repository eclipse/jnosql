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
package org.jnosql.artemis.document.query;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Pagination;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.Sorts;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.model.Vendor;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.Value;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.concurrent.ThreadLocalRandom.current;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jnosql.diana.api.Condition.AND;
import static org.jnosql.diana.api.Condition.BETWEEN;
import static org.jnosql.diana.api.Condition.EQUALS;
import static org.jnosql.diana.api.Condition.GREATER_THAN;
import static org.jnosql.diana.api.Condition.IN;
import static org.jnosql.diana.api.Condition.LESSER_EQUALS_THAN;
import static org.jnosql.diana.api.Condition.LESSER_THAN;
import static org.jnosql.diana.api.Condition.LIKE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(CDIExtension.class)
class DocumentRepositoryProxySortTest {


    private DocumentTemplate template;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private PersonRepository personRepository;



    @BeforeEach
    public void setUp() {
        this.template = Mockito.mock(DocumentTemplate.class);

        DocumentRepositoryProxy personHandler = new DocumentRepositoryProxy(template,
                classMappings, PersonRepository.class, converters);

        when(template.insert(any(Person.class))).thenReturn(Person.builder().build());
        when(template.insert(any(Person.class), any(Duration.class))).thenReturn(Person.builder().build());
        when(template.update(any(Person.class))).thenReturn(Person.builder().build());

        personRepository = (PersonRepository) Proxy.newProxyInstance(PersonRepository.class.getClassLoader(),
                new Class[]{PersonRepository.class},
                personHandler);
    }




    @Test
    public void shouldFindAll() {

        when(template.select(any(DocumentQuery.class))).thenReturn(Collections.singletonList(Person.builder().build()));

        Pagination pagination = getPagination();
        personRepository.findAll(pagination, Sorts.sorts().asc("name"));

        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        verify(template).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals("Person", query.getDocumentCollection());
        assertEquals(pagination.getSkip(), query.getSkip());
        assertEquals(pagination.getLimit(), query.getLimit());
        MatcherAssert.assertThat(query.getSorts(), Matchers.contains(Sort.asc("name")));


    }

       private Pagination getPagination() {
        return Pagination.page(current().nextLong(1, 10)).size(current().nextLong(1, 10));
    }

    interface PersonRepository extends Repository<Person, Long> {

        List<Person> findAll(Pagination pagination, Sorts sorts);

        Person findByName(String name, Pagination pagination, Sort sort);

        List<Person> findByAge(String age, Sort sort);

        List<Person> findByNameAndAge(String name, Integer age, Sorts sorts);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sort sort);

        List<Person> findByNameOrderByName(String name, Pagination pagination, Sorts sorts);


    }

}
