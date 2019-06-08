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


import org.jnosql.artemis.EntityPostPersit;
import org.jnosql.artemis.EntityPrePersist;
import org.jnosql.diana.api.key.KeyValueEntity;

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
    public void firePreKeyValue(KeyValueEntity<?> entity) {
        keyValueEntityPrePersistEvent.fire(KeyValueEntityPrePersist.of(entity));
    }

    @Override
    public void firePostKeyValue(KeyValueEntity<?> entity) {
        keyValueEntityPostPersistEvent.fire(KeyValueEntityPostPersist.of(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersitEvent.fire(EntityPostPersit.of(entity));
    }

    @Override
    public <T> void firePreKeyValueEntity(T entity) {
        entityKeyValuePrePersist.fire(EntityKeyValuePrePersist.of(entity));
    }

    @Override
    public <T> void firePostKeyValueEntity(T entity) {
        entityKeyValuePostPersist.fire(EntityKeyValuePostPersist.of(entity));
    }
}
