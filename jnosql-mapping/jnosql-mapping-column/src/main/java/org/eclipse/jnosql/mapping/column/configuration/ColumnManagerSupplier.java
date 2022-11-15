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

import jakarta.nosql.Settings;
import jakarta.nosql.column.ColumnConfiguration;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnFamilyManagerFactory;
import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.config.MicroProfileSettings;
import org.eclipse.jnosql.mapping.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.CDI;
import java.util.Optional;
import java.util.function.Supplier;

import static org.eclipse.jnosql.mapping.config.MappingConfigurations.COLUMN_DATABASE;
import static org.eclipse.jnosql.mapping.config.MappingConfigurations.COLUMN_PROVIDER;

@ApplicationScoped
class ColumnManagerSupplier implements Supplier<ColumnFamilyManager> {

    @Override
    @Produces
    @ApplicationScoped
    public ColumnFamilyManager get() {
        Settings settings = MicroProfileSettings.INSTANCE;

        ColumnConfiguration configuration = settings.get(COLUMN_PROVIDER, Class.class)
                .filter(c -> ColumnConfiguration.class.isAssignableFrom(c))
                .map(c -> {
                    final Reflections reflections = CDI.current().select(Reflections.class).get();
                    return (ColumnConfiguration) reflections.newInstance(c);
                }).orElseGet(() -> ColumnConfiguration.getConfiguration());

        ColumnFamilyManagerFactory managerFactory = configuration.get(settings);

        Optional<String> database = settings.get(COLUMN_DATABASE, String.class);
        String db = database.orElseThrow(() -> new MappingException("Please, inform the database filling up the property "
                + COLUMN_DATABASE));
        ColumnFamilyManager manager = managerFactory.get(db);
        return manager;
    }

    public void close(@Disposes ColumnFamilyManager manager) {
        manager.close();
    }
}
