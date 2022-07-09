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
package org.eclipse.jnosql.mapping.keyvalue.reactive.configuration;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.keyvalue.reactive.ReactiveKeyValueTemplate;
import org.eclipse.jnosql.mapping.keyvalue.reactive.ReactiveKeyValueTemplateProducer;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

public class ReactiveKeyValueManagerConverter extends AbstractConfiguration<ReactiveKeyValueTemplate>
        implements Converter<ReactiveKeyValueTemplate> {

    @Override
    protected ReactiveKeyValueTemplate success(String value) {
        Config config = CDI.current().select(Config.class).get();
        final KeyValueTemplate template = config.getValue(value, KeyValueTemplate.class);
        ReactiveKeyValueTemplateProducer producer = CDI.current().select(ReactiveKeyValueTemplateProducer.class).get();
        return producer.get(template);
    }
}
