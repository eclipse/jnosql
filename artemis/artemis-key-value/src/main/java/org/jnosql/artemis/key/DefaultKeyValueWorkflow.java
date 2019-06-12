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

import jakarta.nosql.mapping.key.KeyValueEntityConverter;
import jakarta.nosql.mapping.key.KeyValueEventPersistManager;

import javax.inject.Inject;


/**
 * Default implentation of {@link jakarta.nosql.mapping.key.KeyValueWorkflow}
 */
class DefaultKeyValueWorkflow extends AbstractKeyValueWorkflow {

    private KeyValueEventPersistManager eventPersistManager;


    private KeyValueEntityConverter converter;

    DefaultKeyValueWorkflow() {
    }

    @Inject
    DefaultKeyValueWorkflow(KeyValueEventPersistManager eventPersistManager, KeyValueEntityConverter converter) {
        this.eventPersistManager = eventPersistManager;
        this.converter = converter;
    }

    @Override
    protected KeyValueEventPersistManager getEventPersistManager() {
        return eventPersistManager;
    }

    @Override
    protected KeyValueEntityConverter getConverter() {
        return converter;
    }
}
