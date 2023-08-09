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
package org.eclipse.jnosql.mapping.keyvalue.query;

import jakarta.data.repository.PageableRepository;
import jakarta.data.repository.Param;
import jakarta.data.repository.Query;
import jakarta.nosql.PreparedStatement;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.DynamicQueryException;
import org.eclipse.jnosql.mapping.test.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyValueRepositoryProxyTest {

    @Mock
    private KeyValueTemplate template;

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {

        KeyValueRepositoryProxy handler = new KeyValueRepositoryProxy(UserRepository.class, template);
        userRepository = (UserRepository) Proxy.newProxyInstance(UserRepository.class.getClassLoader(),
                new Class[]{UserRepository.class},
                handler);
    }

    @Test
    public void shouldSave() {
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        User user = new User("ada", "Ada", 10);
        userRepository.save(user);
        Mockito.verify(template).put(captor.capture());
        User value = captor.getValue();
        assertEquals(user, value);
    }


    @Test
    public void shouldSaveIterable() {
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);

        User user = new User("ada", "Ada", 10);
        userRepository.saveAll(Collections.singleton(user));
        Mockito.verify(template).put(captor.capture());
        User value = (User) captor.getValue().iterator().next();
        assertEquals(user, value);
    }


    @Test
    public void shouldDelete() {
        userRepository.deleteById("key");
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(template).delete(captor.capture());
        assertEquals("key", captor.getValue());
    }

    @Test
    public void shouldDeleteIterable() {
        userRepository.deleteAllById(Collections.singletonList("key"));
        ArgumentCaptor<Iterable> captor = ArgumentCaptor.forClass(Iterable.class);
        Mockito.verify(template).delete(captor.capture());
        assertEquals("key", captor.getValue().iterator().next());
    }

    @Test
    public void shouldFindById() {
        User user = new User("ada", "Ada", 10);
        when(template.get("key", User.class)).thenReturn(
                Optional.of(user));

        assertEquals(user, userRepository.findById("key").get());
    }

    @Test
    public void shouldExistsById() {
        User user = new User("ada", "Ada", 10);
        when(template.get("key", User.class)).thenReturn(
                Optional.of(user));

        assertThat(userRepository.existsById("key")).isTrue();
        assertThat(userRepository.existsById("non-exist")).isFalse();
    }

    @Test
    public void shouldFindByIdIterable() {
        User user = new User("ada", "Ada", 10);
        User user2 = new User("ada", "Ada", 10);
        List<String> keys = Arrays.asList("key", "key2");
        when(template.get(keys, User.class)).thenReturn(
                Arrays.asList(user, user2));

        assertThat(userRepository.findAllById(keys)).contains(user, user2);
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
        User user = new User("12", "Ada", 10);
        List<String> keys = Arrays.asList("key", "key2");
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        when(template.prepare("get @id", User.class)).thenReturn(prepare);

        userRepository.findByQuery("id");
        verify(template).prepare("get @id", User.class);

    }

    @Test
    public void shouldExecuteDefaultMethod() {
        User user = new User("12", "Otavio", 30);
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        Mockito.when(template.prepare("get @id", User.class))
                .thenReturn(prepare);
        Mockito.when(prepare.result()).thenReturn(Stream.of(user));
        userRepository.otavio();
        verify(template).prepare("get @id", User.class);
        verify(prepare).bind("id", "otavio");
    }

    @Test
    public void shouldUseQueriesFromOtherInterface() {
        User user = new User("12", "Ada", 30);
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        Mockito.when(template.prepare("get @key", User.class))
                .thenReturn(prepare);
        Mockito.when(prepare.result()).thenReturn(Stream.of(user));
        userRepository.key("Ada");
        verify(template).prepare("get @key", User.class);
        verify(prepare).bind("key", "Ada");
    }

    @Test
    public void shouldUseDefaultMethodFromOtherInterface() {
        User user = new User("12", "Poliana", 30);
        PreparedStatement prepare = Mockito.mock(PreparedStatement.class);
        Mockito.when(template.prepare("get @key", User.class))
                .thenReturn(prepare);
        Mockito.when(prepare.result()).thenReturn(Stream.of(user));
        userRepository.poliana();
        verify(template).prepare("get @key", User.class);
        verify(prepare).bind("key", "Poliana");
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
        assertEquals(userRepository.hashCode(), userRepository.hashCode());
    }

    @Test
    public void shouldReturnUnsupportedOperationException() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.findAll());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.count());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.findAll(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.deleteAll());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.delete(null));
        Assertions.assertThrows(UnsupportedOperationException.class, () -> userRepository.deleteAll(null));
    }

    interface BaseQuery<T> {

        @Query("get @key")
        List<T> key(@Param("key") String name);

        default List<T> poliana() {
            return this.key("Poliana");
        }
    }

    interface UserRepository extends PageableRepository<User, String>, BaseQuery<User> {

        Optional<User> findByName(String name);

        @Query("get \"12\"")
        Optional<User> findByQuery();


        @Query("get @id")
        Optional<User> querybyKey(@Param("id") String key);
        default Optional<User> otavio() {
            return querybyKey("otavio");
        }

        @Query("get @id")
        Optional<User> findByQuery(@Param("id") String id);
    }

}