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
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import org.eclipse.jnosql.artemis.configuration.AbstractConfiguration;
import org.eclipse.jnosql.artemis.configuration.SettingsConverter;
import org.eclipse.jnosql.artemis.util.BeanManagers;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

/**
 * Converter the {@link String} to {@link ColumnFamilyManager} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link ColumnConfiguration}
 */
public class ColumnFamilyManagerConverter extends AbstractConfiguration<ColumnFamilyManager>
        implements Converter<ColumnFamilyManager> {

    @Override
    public ColumnFamilyManager success(String value) {
        Config config = BeanManagers.getInstance(Config.class);
        final ColumnFamilyManagerFactory managerFactory = config.getValue(value, ColumnFamilyManagerFactory.class);
        final String database = value + ".database";
        final String entity = config.getValue(database, String.class);
        return managerFactory.get(entity);
    }
}
