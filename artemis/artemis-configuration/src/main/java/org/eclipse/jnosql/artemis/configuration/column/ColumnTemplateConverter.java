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
package org.eclipse.jnosql.artemis.configuration.column;

import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnTemplateProducer;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link ColumnTemplate} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link jakarta.nosql.column.ColumnConfiguration}
 */
public class ColumnTemplateConverter implements Converter<ColumnTemplate> {

    @Override
    public ColumnTemplate convert(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final ColumnFamilyManager manager = config.getValue(value, ColumnFamilyManager.class);
        ColumnTemplateProducer producer = BeanManagers.getInstance(ColumnTemplateProducer.class);

        return producer.get(manager);
    }
}
