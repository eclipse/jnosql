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
import org.eclipse.jnosql.mapping.PreparedStatement;
import org.eclipse.jnosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.communication.keyvalue.KeyValueEntity;
import org.eclipse.jnosql.communication.keyvalue.KeyValuePreparedStatement;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.keyvalue.spi.KeyValueExtension;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.eclipse.jnosql.mapping.keyvalue.entities.Person;
import org.eclipse.jnosql.mapping.keyvalue.entities.User;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Converters.class, KeyValueEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, KeyValueExtension.class})
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DefaultKeyValueTemplateTest {

    private static final String KEY = "otaviojava";
    @Inject
    private KeyValueEntityConverter converter;

    @Inject
    private KeyValueEventPersistManager eventManager;

    @Mock
    private BucketManager manager;

    @Captor
    private ArgumentCaptor<KeyValueEntity> captor;

    private KeyValueTemplate template;


    @BeforeEach
    void setUp() {
        Instance<BucketManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(manager);
        this.template = new DefaultKeyValueTemplate(converter, instance, eventManager);
    }

    @Test
    void shouldPut() {
        User user = new User(KEY, "otavio", 27);
        template.put(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }


    @Test
    void shouldMergeOnPut() {
        User user = new User(KEY, "otavio", 27);
        User result = template.put(user);
        assertSame(user, result);
    }

    @Test
    void shouldPutIterable() {
        User user = new User(KEY, "otavio", 27);
        template.put(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldPutTTL() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.put(user, duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldPutTTLIterable() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.put(singletonList(user), duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldInsert() {
        User user = new User(KEY, "otavio", 27);
        template.insert(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldInsertIterable() {
        User user = new User(KEY, "otavio", 27);
        template.insert(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldInsertTTL() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.insert(user, duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldInsertTTLIterable() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        template.insert(singletonList(user), duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldUpdate() {
        User user = new User(KEY, "otavio", 27);
        template.update(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldUpdateIterable() {
        User user = new User(KEY, "otavio", 27);
        template.update(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.key());
        assertEquals(user, entity.value());
    }

    @Test
    void shouldGet() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        Optional<User> userOptional = template.get(KEY, User.class);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void shouldFindById() {
        User user = new User(KEY, "otavio", 27);
        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        Optional<User> userOptional = template.find(User.class, KEY);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    void shouldGetIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        List<User> userOptional = stream(template.get(singletonList(KEY), User.class).spliterator(), false)
                .toList();

        assertFalse(userOptional.isEmpty());
        assertEquals(user, userOptional.get(0));
    }

    @Test
    void shouldReturnEmptyIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.empty());
        List<User> userOptional = stream(template.get(singletonList(KEY), User.class).spliterator(), false)
                .toList();

        assertTrue(userOptional.isEmpty());
    }

    @Test
    void shouldRemove() {
        template.delete(KEY);
        Mockito.verify(manager).delete(KEY);
    }


    @Test
    void shouldRemoveById() {
        template.delete(User.class, KEY);
        Mockito.verify(manager).delete(KEY);
    }

    @Test
    void shouldRemoveIterable() {
        template.delete(singletonList(KEY));
        Mockito.verify(manager).delete(singletonList(KEY));
    }

    @Test
    void shouldExecuteClass() {
        template.query("remove id");
        Mockito.verify(manager).query("remove id");
    }

    @Test
    @MockitoSettings(strictness = Strictness.LENIENT)
    void shouldReturnErrorWhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> template.query(null));
        assertThrows(NullPointerException.class, () -> template.query(null, String.class));
    }

    @Test
    void shouldExecuteClassNotClass() {
        template.query("remove id");
        Mockito.verify(manager).query("remove id");
    }

    @Test
    void shouldExecuteQuery() {
        when(manager.query("get id"))
                .thenReturn(Stream.of(Value.of("12")));

        List<Integer> ids = template.query("get id", Integer.class).collect(toList());
        assertThat(ids).contains(12);
    }

    @Test
    void shouldReturnSingleResult() {
        when(manager.query("get id"))
                .thenReturn(Stream.of(Value.of("12")));

        Optional<Integer> id = template.getSingleResult("get id", Integer.class);
        assertTrue(id.isPresent());
    }

    @Test
    void shouldReturnSingleResult2() {

        when(manager.query("get id2"))
                .thenReturn(Stream.empty());

        assertFalse(template.getSingleResult("get id2", Integer.class).isPresent());
    }

    @Test
    void shouldReturnSingleResult3() {
        when(manager.query("get id3"))
                .thenReturn(Stream.of(Value.of("12"), Value.of("15")));
        assertThrows(NonUniqueResultException.class, () -> template.getSingleResult("get id3", Integer.class));
    }

    @Test
    void shouldExecutePrepare() {
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
    void shouldUnsupportedExceptionOnSelect() {
        assertThrows(UnsupportedOperationException.class, ()-> template.select(Person.class));
    }

    @Test
    void shouldUnsupportedExceptionOnDelete() {
        assertThrows(UnsupportedOperationException.class, ()-> template.delete(Person.class));
    }
}
