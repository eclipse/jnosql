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
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.PreparedStatement;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.communication.keyvalue.KeyValueEntity;
import org.eclipse.jnosql.communication.keyvalue.KeyValuePreparedStatement;
import org.eclipse.jnosql.mapping.Convert;
import org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
import org.eclipse.jnosql.mapping.reflection.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.test.entities.Person;
import org.eclipse.jnosql.mapping.test.entities.User;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@EnableAutoWeld
@AddPackages(value = {Convert.class, KeyValueWorkflow.class})
@AddPackages(MockProducer.class)
@AddExtensions({EntityMetadataExtension.class, KeyValueExtension.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class DefaultKeyValueTemplateTest {

    private static final String KEY = "otaviojava";
    @Inject
    private KeyValueEntityConverter converter;

    @Inject
    private KeyValueWorkflow flow;

    @Inject
    private KeyValueEventPersistManager eventManager;

    @Mock
    private BucketManager manager;

    @Captor
    private ArgumentCaptor<KeyValueEntity> captor;

    private KeyValueTemplate template;


    @BeforeEach
    public void setUp() {
        Instance<BucketManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(manager);
        this.template = new DefaultKeyValueTemplate(converter, instance, flow, eventManager);
    }

    @Test
    public void shouldPut() {
        User user = new User(KEY, "otavio", 27);
        template.put(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }


    @Test
    public void shouldMergeOnPut() {
        User user = new User(KEY, "otavio", 27);
        User result = template.put(user);
        assertSame(user, result);
    }

    @Test
    public void shouldPutIterable() {
        User user = new User(KEY, "otavio", 27);
        template.put(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldPutTTL() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.put(user, duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldPutTTLIterable() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.put(singletonList(user), duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldInsert() {
        User user = new User(KEY, "otavio", 27);
        template.insert(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldInsertIterable() {
        User user = new User(KEY, "otavio", 27);
        template.insert(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldInsertTTL() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.insert(user, duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldInsertTTLIterable() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.insert(singletonList(user), duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldUpdate() {
        User user = new User(KEY, "otavio", 27);
        template.update(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldUpdateIterable() {
        User user = new User(KEY, "otavio", 27);
        template.update(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    public void shouldGet() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        Optional<User> userOptional = template.get(KEY, User.class);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    public void shouldFindById() {
        User user = new User(KEY, "otavio", 27);
        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        Optional<User> userOptional = template.find(User.class, KEY);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    public void shouldGetIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        List<User> userOptional = stream(template.get(singletonList(KEY), User.class).spliterator(), false)
                .collect(toList());

        assertFalse(userOptional.isEmpty());
        assertEquals(user, userOptional.get(0));
    }

    @Test
    public void shouldReturnEmptyIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.empty());
        List<User> userOptional = stream(template.get(singletonList(KEY), User.class).spliterator(), false)
                .collect(toList());

        assertTrue(userOptional.isEmpty());
    }

    @Test
    public void shouldRemove() {
        template.delete(KEY);
        Mockito.verify(manager).delete(KEY);
    }

    @Test
    public void shouldRemoveById() {
        template.delete(User.class, KEY);
        Mockito.verify(manager).delete(KEY);
    }

    @Test
    public void shouldRemoveIterable() {
        template.delete(singletonList(KEY));
        Mockito.verify(manager).delete(singletonList(KEY));
    }

    @Test
    public void shouldExecuteClass() {
        template.query("remove id");
        Mockito.verify(manager).query("remove id");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    public void shouldReturnErrorWhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> template.query(null));
        assertThrows(NullPointerException.class, () -> template.query(null, String.class));
    }

    @Test
    public void shouldExecuteClassNotClass() {
        template.query("remove id");
        Mockito.verify(manager).query("remove id");
    }

    @Test
    public void shouldExecuteQuery() {
        when(manager.query("get id"))
                .thenReturn(Stream.of(Value.of("12")));

        List<Integer> ids = template.query("get id", Integer.class).collect(toList());
        assertThat(ids).contains(12);
    }

    @Test
    public void shouldReturnSingleResult() {
        when(manager.query("get id"))
                .thenReturn(Stream.of(Value.of("12")));

        Optional<Integer> id = template.getSingleResult("get id", Integer.class);
        assertTrue(id.isPresent());
    }

    @Test
    public void shouldReturnSingleResult2() {

        when(manager.query("get id2"))
                .thenReturn(Stream.empty());

        assertFalse(template.getSingleResult("get id2", Integer.class).isPresent());
    }

    @Test
    public void shouldReturnSingleResult3() {
        when(manager.query("get id3"))
                .thenReturn(Stream.of(Value.of("12"), Value.of("15")));
        assertThrows(NonUniqueResultException.class, () -> template.getSingleResult("get id3", Integer.class));
    }

    @Test
    public void shouldExecutePrepare() {
        org.eclipse.jnosql.communication.keyvalue.KeyValuePreparedStatement prepare = Mockito.mock(KeyValuePreparedStatement.class);
        when(prepare.result()).thenReturn(Stream.of(Value.of("12")));
        when(prepare.singleResult()).thenReturn(Optional.of(Value.of("12")));
        when(manager.prepare("get @id")).thenReturn(prepare);

        PreparedStatement statement = template.prepare("get @id", Integer.class);
        statement.bind("id", 12);
        List<Integer> resultList = statement.<Integer>result().collect(toList());
        assertThat(resultList).contains(12);
        Optional<Object> singleResult = statement.singleResult();
        assertTrue(singleResult.isPresent());
        assertEquals(12, singleResult.get());
    }

    @Test
    public void shouldUnsupportedExceptionOnSelect() {
        assertThrows(UnsupportedOperationException.class, ()-> template.select(Person.class));
    }

    @Test
    public void shouldUnsupportedExceptionOnDelete() {
        assertThrows(UnsupportedOperationException.class, ()-> template.delete(Person.class));
    }
}