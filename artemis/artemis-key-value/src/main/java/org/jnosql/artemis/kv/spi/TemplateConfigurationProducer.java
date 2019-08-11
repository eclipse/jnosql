/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.kv.spi;

import jakarta.nosql.mapping.ConfigurationUnit;
import jakarta.nosql.mapping.kv.KeyValueTemplate;
import jakarta.nosql.mapping.kv.KeyValueTemplateProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * It creates a {@link KeyValueTemplate} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class TemplateConfigurationProducer {

    @Inject
    private KeyValueTemplateProducer producer;

    @Inject
    private ManagerConfigurationProducer managerProducer;

    @ConfigurationUnit
    @Produces
    public KeyValueTemplate get(InjectionPoint injectionPoint) {
        return producer.get(managerProducer.get(injectionPoint));
    }

}
