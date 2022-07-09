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
package org.eclipse.jnosql.mapping.column.reactive.configuration;

import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.mapping.column.reactive.ReactiveColumnTemplate;
import org.eclipse.jnosql.mapping.column.reactive.ReactiveColumnTemplateProducer;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

public class ReactiveColumnManagerConverter  extends AbstractConfiguration<ReactiveColumnTemplate>
        implements Converter<ReactiveColumnTemplate> {

    @Override
    protected ReactiveColumnTemplate success(String value) {
        Config config = CDI.current().select(Config.class).get();
        final ColumnTemplate template = config.getValue(value, ColumnTemplate.class);
        ReactiveColumnTemplateProducer producer = CDI.current().select(ReactiveColumnTemplateProducer.class).get();
        return producer.get(template);
    }
}