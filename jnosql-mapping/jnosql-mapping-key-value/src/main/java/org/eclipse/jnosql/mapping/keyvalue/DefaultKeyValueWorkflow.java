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

import jakarta.nosql.mapping.keyvalue.KeyValueEntityConverter;
import jakarta.nosql.mapping.keyvalue.KeyValueEventPersistManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;


/**
 * Default implementation of {@link jakarta.nosql.mapping.keyvalue.KeyValueWorkflow}
 */
@ApplicationScoped
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
