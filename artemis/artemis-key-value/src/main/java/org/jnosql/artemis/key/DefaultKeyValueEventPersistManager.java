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


import jakarta.nosql.kv.KeyValueEntity;
import jakarta.nosql.mapping.EntityPostPersit;
import jakarta.nosql.mapping.EntityPrePersist;
import jakarta.nosql.mapping.kv.EntityKeyValuePostPersist;
import jakarta.nosql.mapping.kv.EntityKeyValuePrePersist;
import jakarta.nosql.mapping.kv.KeyValueEntityPostPersist;
import jakarta.nosql.mapping.kv.KeyValueEntityPrePersist;
import jakarta.nosql.mapping.kv.KeyValueEventPersistManager;
import org.jnosql.artemis.DefaultEntityPostPersist;
import org.jnosql.artemis.DefaultEntityPrePersist;

import javax.enterprise.event.Event;
import javax.inject.Inject;

class DefaultKeyValueEventPersistManager implements KeyValueEventPersistManager {

    @Inject
    private Event<KeyValueEntityPrePersist> keyValueEntityPrePersistEvent;

    @Inject
    private Event<KeyValueEntityPostPersist> keyValueEntityPostPersistEvent;

    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Inject
    private Event<EntityKeyValuePrePersist> entityKeyValuePrePersist;

    @Inject
    private Event<EntityKeyValuePostPersist> entityKeyValuePostPersist;


    @Override
    public void firePreKeyValue(KeyValueEntity entity) {
        keyValueEntityPrePersistEvent.fire(new DefaultKeyValueEntityPrePersist(entity));
    }

    @Override
    public void firePostKeyValue(KeyValueEntity entity) {
        keyValueEntityPostPersistEvent.fire(new DefaultKeyValueEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(new DefaultEntityPrePersist(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersitEvent.fire(new DefaultEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreKeyValueEntity(T entity) {
        entityKeyValuePrePersist.fire(new DefaultEntityKeyValuePrePersist(entity));
    }

    @Override
    public <T> void firePostKeyValueEntity(T entity) {
        entityKeyValuePostPersist.fire(new DefaultEntityKeyValuePostPersist(entity));
    }
}
