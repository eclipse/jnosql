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



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import jakarta.nosql.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;

import java.util.Objects;
import java.util.function.Function;


/**
 * The producer of {@link KeyValueTemplate}
 */
@ApplicationScoped
public class KeyValueTemplateProducer implements Function<BucketManager, KeyValueTemplate> {

    @Inject
    private KeyValueEntityConverter converter;
    @Inject
    private KeyValueWorkflow flow;

    @Inject
    private KeyValueEventPersistManager eventManager;

    @Override
    public KeyValueTemplate apply(BucketManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return new ProducerKeyValueTemplate(converter, flow, manager, eventManager);
    }

    @Vetoed
    static class ProducerKeyValueTemplate extends AbstractKeyValueTemplate {

        private KeyValueEntityConverter converter;

        private KeyValueWorkflow flow;

        private BucketManager manager;

        private KeyValueEventPersistManager eventManager;

        ProducerKeyValueTemplate(KeyValueEntityConverter converter, KeyValueWorkflow flow,
                                 BucketManager manager, KeyValueEventPersistManager eventManager) {
            this.converter = converter;
            this.flow = flow;
            this.manager = manager;
            this.eventManager = eventManager;
        }

        ProducerKeyValueTemplate() {
        }

        @Override
        protected KeyValueEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected BucketManager getManager() {
            return manager;
        }

        @Override
        protected KeyValueWorkflow getFlow() {
            return flow;
        }

        @Override
        protected KeyValueEventPersistManager getEventManager() {
            return eventManager;
        }
    }
}
