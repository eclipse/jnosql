/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.keyvalue;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.entities.Person;
import org.eclipse.microprofile.reactive.streams.operators.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.operators.ReactiveStreams;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import javax.enterprise.inject.Instance;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.WARN)
class DefaultReactiveKeyValueManagerTest {

    @Mock
    private KeyValueTemplate template;

    @Mock
    private Instance<KeyValueTemplate> instance;

    private ReactiveKeyValueManager manager;

    @BeforeEach
    public void setUp() {
        Mockito.when(instance.get()).thenReturn(template);
        this.manager = new DefaultReactiveKeyValueManager(instance);
    }

    @Test
    public void shouldPut() {

        AtomicReference<Person> reference = new AtomicReference<>();

        Person ada = Person.builder()
                .withId(1L)
                .withAge(30)
                .withName("Ada").build();
        Mockito.when(template.put(ada)).thenReturn(ada);
        final Publisher<Person> publisher = manager.put(ada);
        CompletionSubscriber<Person, Optional<Person>> subscriber = ReactiveStreams.<Person>builder().findFirst().build();
        Mockito.verify(template, Mockito.never()).put(ada);

        publisher.subscribe(subscriber);
        CompletionStage<Optional<Person>> completion = subscriber.getCompletion();
        completion.thenAccept(o -> reference.set(o.get()));
        Mockito.verify(template).put(ada);
        assertEquals(ada, reference.get());

    }


}