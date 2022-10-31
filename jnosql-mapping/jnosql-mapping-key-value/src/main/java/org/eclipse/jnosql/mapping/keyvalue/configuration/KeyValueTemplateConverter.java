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
package org.eclipse.jnosql.mapping.keyvalue.configuration;

import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueConfiguration;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplateProducer;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

/**
 * Converter the {@link String} to {@link KeyValueTemplate} it will use the
 * {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link KeyValueConfiguration}
 */
public class KeyValueTemplateConverter extends AbstractConfiguration<KeyValueTemplate>
        implements Converter<KeyValueTemplate> {

    @Override
    public KeyValueTemplate success(String value) {
        Config config = CDI.current().select(Config.class).get();
        final BucketManager manager = config.getValue(value, BucketManager.class);
        KeyValueTemplateProducer producer = CDI.current().select(KeyValueTemplateProducer.class).get();

        return producer.get(manager);
    }
}
