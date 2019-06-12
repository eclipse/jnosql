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

import jakarta.nosql.mapping.EntityPostPersit;
import jakarta.nosql.mapping.EntityPrePersist;
import jakarta.nosql.key.KeyValueEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.event.Event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DefaultKeyValueEventPersistManagerTest {

    @InjectMocks
    private DefaultKeyValueEventPersistManager subject;

    @Mock
    private Event<KeyValueEntityPrePersist> keyValueEntityPrePersistEvent;

    @Mock
    private Event<KeyValueEntityPostPersist> keyValueEntityPostPersistEvent;

    @Mock
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Mock
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Mock
    private Event<EntityKeyValuePrePersist> entityKeyValuePrePersist;

    @Mock
    private Event<EntityKeyValuePostPersist> entityKeyValuePostPersist;

    @Test
    public void shouldFirePreColumn() {
        KeyValueEntity entity = KeyValueEntity.of("key", "value");
        subject.firePreKeyValue(entity);
        ArgumentCaptor<KeyValueEntityPrePersist> captor = ArgumentCaptor.forClass(KeyValueEntityPrePersist.class);
        verify(keyValueEntityPrePersistEvent).fire(captor.capture());

        KeyValueEntityPrePersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }


    @Test
    public void shouldFirePostColumn() {
        KeyValueEntity entity = KeyValueEntity.of("key", "value");
        subject.firePostKeyValue(entity);
        ArgumentCaptor<KeyValueEntityPostPersist> captor = ArgumentCaptor.forClass(KeyValueEntityPostPersist.class);
        verify(keyValueEntityPostPersistEvent).fire(captor.capture());

        KeyValueEntityPostPersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }

    @Test
    public void shouldFirePreEntity() {
        Actor actor = new Actor();
        actor.name = "Luke";
        subject.firePreEntity(actor);
        ArgumentCaptor<EntityPrePersist> captor = ArgumentCaptor.forClass(EntityPrePersist.class);
        verify(entityPrePersistEvent).fire(captor.capture());
        EntityPrePersist value = captor.getValue();
        assertEquals(actor, value.getValue());
    }

    @Test
    public void shouldFirePostEntity() {
        Actor actor = new Actor();
        actor.name = "Luke";
        subject.firePostEntity(actor);
        ArgumentCaptor<EntityPostPersit> captor = ArgumentCaptor.forClass(EntityPostPersit.class);
        verify(entityPostPersitEvent).fire(captor.capture());
        EntityPostPersit value = captor.getValue();
        assertEquals(actor, value.getValue());
    }

    @Test
    public void shouldFirePreKeyValueEntity() {
        Actor actor = new Actor();
        actor.name = "Luke";
        subject.firePreKeyValueEntity(actor);
        ArgumentCaptor<EntityKeyValuePrePersist> captor = ArgumentCaptor.forClass(EntityKeyValuePrePersist.class);
        verify(entityKeyValuePrePersist).fire(captor.capture());
        EntityKeyValuePrePersist value = captor.getValue();
        assertEquals(actor, value.getValue());
    }

    @Test
    public void shouldFirePostColumnEntity() {
        Actor actor = new Actor();
        actor.name = "Luke";
        subject.firePostKeyValueEntity(actor);
        ArgumentCaptor<EntityKeyValuePostPersist> captor = ArgumentCaptor.forClass(EntityKeyValuePostPersist.class);
        verify(entityKeyValuePostPersist).fire(captor.capture());
        EntityKeyValuePostPersist value = captor.getValue();
        assertEquals(actor, value.getValue());
    }

    class Actor {
        private String name;
    }

}