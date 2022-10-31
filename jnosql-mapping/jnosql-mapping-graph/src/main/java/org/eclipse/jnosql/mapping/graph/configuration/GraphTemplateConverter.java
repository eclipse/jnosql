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
package org.eclipse.jnosql.mapping.graph.configuration;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.jnosql.mapping.graph.GraphConfiguration;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.graph.GraphTemplateProducer;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

/**
 * Converter the {@link String} to {@link GraphTemplate} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link GraphConfiguration}
 */
public class GraphTemplateConverter  extends AbstractConfiguration<GraphTemplate> implements Converter<GraphTemplate> {

    @Override
    public GraphTemplate success(String value) {

        Config config = CDI.current().select(Config.class).get();
        final Graph manager = config.getValue(value, Graph.class);
        GraphTemplateProducer producer =  CDI.current().select(GraphTemplateProducer.class).get();
        return producer.get(manager);
    }
}
