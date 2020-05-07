/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.keyvalue.reactive.query;

import jakarta.nosql.mapping.DynamicQueryException;
import jakarta.nosql.mapping.Param;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.Query;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.User;
import org.eclipse.jnosql.artemis.keyvalue.reactive.ReactiveKeyValueTemplate;
import org.eclipse.jnosql.artemis.reactive.Observable;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.WARN)
public class KeyValueRepositoryProxyTest {

    @Mock
    private KeyValueTemplate template;

    @Mock
    private ReactiveKeyValueTemplate reactiveTemplate;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        ReactiveKeyValueRepositoryProxy handler = new ReactiveKeyValueRepositoryProxy(template, reactiveTemplate, UserRepository.class);
        userRepository = (UserRepository) Proxy.newProxyInstance(UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                handler);
    }

    @Test
    public void shouldSave() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User user = new User("ada", "Ada", 10);
        userRepository.save(user);
        Mockito.verify(reactiveTemplate).put(captor.capture());
        User value = captor.getValue();
        assertEquals(user, value);
    }

    @Test
    public void shouldSaveIterable() {
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);

        User user = new User("ada", "Ada", 10);
        userRepository.save(Collections.singleton(user));
        Mockito.verify(reactiveTemplate).put(captor.capture());
        User value = (User) captor.getValue().iterator().next();
        assertEquals(user, value);
    }


    @Test
    public void shouldDelete() {
        final String key = "key";
        userRepository.deleteById(key);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(template, Mockito.never()).delete(key);
        Mockito.verify(reactiveTemplate).delete(captor.capture());
        assertEquals(key, captor.getValue());
    }

    @Test
    public void shouldDeleteIterable() {
        final List<String> ids = Collections.singletonList("key");
        userRepository.deleteById(ids);
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(template, Mockito.never()).delete(ids);
        Mockito.verify(reactiveTemplate).delete(captor.capture());
        assertEquals("key", captor.getValue().iterator().next());
    }

    @Test
    public void shouldFindById() {
        User user = new User("ada", "Ada", 10);
        when(template.get("key", User.class)).thenReturn(
                Optional.of(user));

        final Observable<User> key = userRepository.findById("key");
        Mockito.verify(reactiveTemplate).get("key", User.class);
    }

    @Test
    public void shouldFindByIdIterable() {
        User user = new User("ada", "Ada", 10);
        User user2 = new User("ada", "Ada", 10);
        List<String> keys = Arrays.asList("key", "key2");
        when(template.get(keys, User.class)).thenReturn(
                Arrays.asList(user, user2));
        userRepository.findById(keys);
        Mockito.verify(reactiveTemplate).get(keys, User.class);
    }

    @Test
    public void shouldFindByQuery() {
        User user = new User("12", "Ada", 10);
        when(template.query("get \"12\"", User.class)).thenReturn(Stream.of(user));

        userRepository.findByQuery();
        verify(template).query("get \"12\"", User.class);

    }

    @Test
    public void shouldFindByQueryWithParameter() {
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        when(template.prepare("get @id", User.class)).thenReturn(prepare);

        userRepository.findByQuery("id");
        verify(template).prepare("get @id", User.class);

    }

    @Test
    public void shouldReturnErrorWhenExecuteMethodQuery() {
        Assertions.assertThrows(DynamicQueryException.class, () -> userRepository.findByName("name"));
    }

    @Test
    public void shouldReturnToString() {
        assertNotNull(userRepository.toString());
    }

    @Test
    public void shouldReturnHasCode() {
        assertNotNull(userRepository.hashCode());
        assertEquals(userRepository.hashCode(), userRepository.hashCode());
    }

    @Test
    public void shouldReturnEquals() {
        assertNotNull(userRepository.equals(userRepository));
    }

    interface UserRepository extends ReactiveRepository<User, String> {

        Optional<User> findByName(String name);

        @Query("get \"12\"")
        Optional<User> findByQuery();

        @Query("get @id")
        Publisher<User> findByQuery(@Param("id") String id);
    }

}