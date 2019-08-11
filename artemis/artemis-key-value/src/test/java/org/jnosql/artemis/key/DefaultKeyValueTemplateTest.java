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
package org.jnosql.artemis.key;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Value;
import jakarta.nosql.kv.BucketManager;
import jakarta.nosql.kv.KeyValueEntity;
import jakarta.nosql.kv.KeyValuePreparedStatement;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.kv.KeyValueEntityConverter;
import jakarta.nosql.mapping.kv.KeyValueTemplate;
import jakarta.nosql.mapping.kv.KeyValueWorkflow;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(CDIExtension.class)
@ExtendWith(MockitoExtension.class)
public class DefaultKeyValueTemplateTest {

    private static final String KEY = "otaviojava";
    @Inject
    private KeyValueEntityConverter converter;

    @Inject
    private KeyValueWorkflow flow;

    @Mock
    private BucketManager manager;

    @Captor
    private ArgumentCaptor<KeyValueEntity> captor;

    private KeyValueTemplate subject;


    @BeforeEach
    public void setUp() {
        Instance<BucketManager> instance = Mockito.mock(Instance.class);
        when(instance.get()).thenReturn(manager);
        this.subject = new DefaultKeyValueTemplate(converter, instance, flow);
    }

    @Test
    public void shouldPut() {
        User user = new User(KEY, "otavio", 27);
        subject.put(user);
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.getKey());
        assertEquals(user, entity.getValue());
    }


    @Test
    public void shouldMergeOnPut() {
        User user = new User(KEY, "otavio", 27);
        User result = subject.put(user);
        assertTrue(user == result);
    }

    @Test
    public void shouldPutIterable() {
        User user = new User(KEY, "otavio", 27);
        subject.put(singletonList(user));
        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.getKey());
        assertEquals(user, entity.getValue());
    }

    @Test
    public void shouldPutTTL() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        subject.put(user, duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.getKey());
        assertEquals(user, entity.getValue());
    }

    @Test
    public void shouldPutTTLIterable() {

        Duration duration = Duration.ofSeconds(2L);
        User user = new User(KEY, "otavio", 27);
        subject.put(singletonList(user), duration);

        Mockito.verify(manager).put(captor.capture(), Mockito.eq(duration));
        KeyValueEntity entity = captor.getValue();
        assertEquals(KEY, entity.getKey());
        assertEquals(user, entity.getValue());
    }


    @Test
    public void shouldGet() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        Optional<User> userOptional = subject.get(KEY, User.class);

        assertTrue(userOptional.isPresent());
        assertEquals(user, userOptional.get());
    }

    @Test
    public void shouldGetIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.of(Value.of(user)));
        List<User> userOptional = stream(subject.get(singletonList(KEY), User.class).spliterator(), false)
                .collect(toList());

        assertFalse(userOptional.isEmpty());
        assertEquals(user, userOptional.get(0));
    }

    @Test
    public void shouldReturnEmptyIterable() {
        User user = new User(KEY, "otavio", 27);

        when(manager.get(KEY)).thenReturn(Optional.empty());
        List<User> userOptional = stream(subject.get(singletonList(KEY), User.class).spliterator(), false)
                .collect(toList());

        assertTrue(userOptional.isEmpty());
    }

    @Test
    public void shouldRemove() {
        subject.remove(KEY);
        Mockito.verify(manager).remove(KEY);
    }

    @Test
    public void shouldRemoveIterable() {
        subject.remove(singletonList(KEY));
        Mockito.verify(manager).remove(singletonList(KEY));
    }

    @Test
    public void shouldExecuteClass() {
        subject.query("remove id");
        Mockito.verify(manager).query("remove id");
    }

    @Test
    public void shouldReturnErrorWhenQueryIsNull() {
        assertThrows(NullPointerException.class, () -> subject.query(null));

        assertThrows(NullPointerException.class, () -> {
            when(manager.query("get id"))
                    .thenReturn(singletonList(Value.of("value")));
            subject.query("get id", null);
        });

        assertThrows(NullPointerException.class, () -> subject.query(null, String.class));
    }

    @Test
    public void shouldExecuteClassNotClass() {
        subject.query("remove id", null);
        Mockito.verify(manager).query("remove id");
    }

    @Test
    public void shouldExecuteQuery() {
        when(manager.query("get id"))
                .thenReturn(singletonList(Value.of("12")));

        List<Integer> ids = subject.query("get id", Integer.class);
        MatcherAssert.assertThat(ids, Matchers.contains(12));
    }

    @Test
    public void shouldReturnSingleResult() {
        when(manager.query("get id"))
                .thenReturn(singletonList(Value.of("12")));

        Optional<Integer> id = subject.getSingleResult("get id", Integer.class);
        assertTrue(id.isPresent());
    }

    @Test
    public void shouldReturnSingleResult2() {

        when(manager.query("get id2"))
                .thenReturn(Collections.emptyList());


        assertFalse(subject.getSingleResult("get id2", Integer.class).isPresent());
    }


    @Test
    public void shouldReturnSingleResult3() {
        when(manager.query("get id3"))
                .thenReturn(Arrays.asList(Value.of("12"), Value.of("15")));

        assertThrows(NonUniqueResultException.class, () -> subject.getSingleResult("get id3", Integer.class));
    }


    @Test
    public void shouldExecutePrepare() {
        KeyValuePreparedStatement prepare = Mockito.mock(KeyValuePreparedStatement.class);
        when(prepare.getResultList()).thenReturn(singletonList(Value.of("12")));
        when(prepare.getSingleResult()).thenReturn(Optional.of(Value.of("12")));
        when(manager.prepare("get @id")).thenReturn(prepare);

        PreparedStatement statement = subject.prepare("get @id", Integer.class);
        statement.bind("id", 12);
        List<Integer> resultList = statement.getResultList();
        MatcherAssert.assertThat(resultList, Matchers.contains(12));
        Optional<Object> singleResult = statement.getSingleResult();
        assertTrue(singleResult.isPresent());
        assertEquals(12, singleResult.get());
    }


}