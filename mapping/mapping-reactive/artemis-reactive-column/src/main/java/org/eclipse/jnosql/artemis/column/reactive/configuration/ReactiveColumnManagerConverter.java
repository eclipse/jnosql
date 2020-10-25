/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.column.reactive.configuration;

import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplate;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplateProducer;
import org.eclipse.jnosql.artemis.configuration.AbstractConfiguration;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

public class ReactiveColumnManagerConverter  extends AbstractConfiguration<ReactiveColumnTemplate>
        implements Converter<ReactiveColumnTemplate> {

    @Override
    protected ReactiveColumnTemplate success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final ColumnTemplate template = config.getValue(value, ColumnTemplate.class);
        ReactiveColumnTemplateProducer producer = BeanManagers.getInstance(ReactiveColumnTemplateProducer.class);
        return producer.get(template);
    }
}