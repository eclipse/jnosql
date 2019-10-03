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
package org.eclipse.jnosql.artemis.graph.configuration;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.graph.GraphTemplate;
import org.eclipse.jnosql.artemis.graph.GraphTemplateProducer;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link GraphTemplate} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link org.eclipse.jnosql.artemis.graph.GraphConfiguration}
 */
public class GraphTemplateConverter implements Converter<GraphTemplate> {

    @Override
    public GraphTemplate convert(String value) {

        Config config = BeanManagers.getInstance(Config.class);
        final Graph manager = config.getValue(value, Graph.class);
        GraphTemplateProducer producer = BeanManagers.getInstance(GraphTemplateProducer.class);
        return producer.get(manager);
    }
}
