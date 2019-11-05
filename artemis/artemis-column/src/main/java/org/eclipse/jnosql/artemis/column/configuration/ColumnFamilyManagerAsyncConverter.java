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
package org.eclipse.jnosql.artemis.column.configuration;

import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import org.eclipse.jnosql.artemis.configuration.AbstractConfiguration;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link ColumnFamilyManagerAsync} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link ColumnConfiguration}
 */
public class ColumnFamilyManagerAsyncConverter extends AbstractConfiguration<ColumnFamilyManagerAsync>
        implements Converter<ColumnFamilyManagerAsync> {

    @Override
    public ColumnFamilyManagerAsync success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final ColumnFamilyManagerAsyncFactory managerFactory = config.getValue(value,
                ColumnFamilyManagerAsyncFactory.class);
        final String database = value + ".database";
        final String entity = config.getValue(database, String.class);
        return managerFactory.getAsync(entity);
    }
}
