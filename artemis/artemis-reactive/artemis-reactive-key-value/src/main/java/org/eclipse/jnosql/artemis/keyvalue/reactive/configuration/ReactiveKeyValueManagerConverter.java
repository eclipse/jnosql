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
package org.eclipse.jnosql.artemis.keyvalue.reactive.configuration;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.artemis.configuration.AbstractConfiguration;
import org.eclipse.jnosql.artemis.keyvalue.reactive.ReactiveKeyValueManager;
import org.eclipse.jnosql.artemis.keyvalue.reactive.ReactiveKeyValueTemplateProducer;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

public class ReactiveKeyValueManagerConverter extends AbstractConfiguration<ReactiveKeyValueManager>
        implements Converter<ReactiveKeyValueManager> {

    @Override
    protected ReactiveKeyValueManager success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final KeyValueTemplate template = config.getValue(value, KeyValueTemplate.class);
        ReactiveKeyValueTemplateProducer producer = BeanManagers.getInstance(ReactiveKeyValueTemplateProducer.class);
        return producer.get(template);
    }
}
