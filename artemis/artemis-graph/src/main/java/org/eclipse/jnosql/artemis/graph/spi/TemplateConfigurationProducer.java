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
package org.eclipse.jnosql.artemis.graph.spi;

import jakarta.nosql.mapping.ConfigurationUnit;
import org.eclipse.jnosql.artemis.graph.GraphTemplate;
import org.eclipse.jnosql.artemis.graph.GraphTemplateProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

/**
 * It creates a {@link GraphTemplate} from a ConfigurationUnit annotation.
 */
@ApplicationScoped
class TemplateConfigurationProducer {

    @Inject
    private GraphConfigurationProducer configurationProducer;

    @Inject
    private GraphTemplateProducer producer;


    @ConfigurationUnit
    @Produces
    public GraphTemplate get(InjectionPoint injectionPoint) {
        return producer.get(configurationProducer.get(injectionPoint));
    }
}
