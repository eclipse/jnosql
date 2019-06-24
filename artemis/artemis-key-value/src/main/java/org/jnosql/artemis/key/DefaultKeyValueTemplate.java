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

import jakarta.nosql.key.BucketManager;
import jakarta.nosql.mapping.key.KeyValueEntityConverter;
import jakarta.nosql.mapping.key.KeyValueWorkflow;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;


class DefaultKeyValueTemplate extends AbstractKeyValueTemplate {

    private KeyValueEntityConverter converter;

    private Instance<BucketManager> manager;


    private KeyValueWorkflow flow;

    @Inject
    DefaultKeyValueTemplate(KeyValueEntityConverter converter, Instance<BucketManager> manager, KeyValueWorkflow flow) {
        this.converter = converter;
        this.manager = manager;
        this.flow = flow;
    }

    DefaultKeyValueTemplate() {
    }

    @Override
    protected KeyValueEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected BucketManager getManager() {
        return manager.get();
    }

    @Override
    protected KeyValueWorkflow getFlow() {
        return flow;
    }
}
