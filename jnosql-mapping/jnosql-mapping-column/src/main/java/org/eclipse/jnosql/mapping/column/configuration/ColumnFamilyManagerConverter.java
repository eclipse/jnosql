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
package org.eclipse.jnosql.mapping.column.configuration;

import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import org.eclipse.jnosql.mapping.configuration.AbstractConfiguration;
import org.eclipse.jnosql.mapping.configuration.SettingsConverter;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.Converter;

import javax.enterprise.inject.spi.CDI;

/**
 * Converter the {@link String} to {@link ColumnFamilyManager} it will use the {@link SettingsConverter} and
 * find by the provider that should be an implementation of {@link ColumnConfiguration}
 */
public class ColumnFamilyManagerConverter extends AbstractConfiguration<ColumnFamilyManager>
        implements Converter<ColumnFamilyManager> {

    @Override
    public ColumnFamilyManager success(String value) {
        Config config = CDI.current().select(Config.class).get();
        final ColumnFamilyManagerFactory managerFactory = config.getValue(value, ColumnFamilyManagerFactory.class);
        final String database = value + ".database";
        final String entity = config.getValue(database, String.class);
        return managerFactory.get(entity);
    }
}
