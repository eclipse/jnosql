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
package org.eclipse.jnosql.mapping.column.spi;


import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessProducer;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.mapping.DatabaseMetadata;
import org.eclipse.jnosql.mapping.Databases;
import org.eclipse.jnosql.mapping.column.query.RepositoryColumnBean;
import org.eclipse.jnosql.mapping.reflection.ClassScanner;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static org.eclipse.jnosql.mapping.DatabaseType.COLUMN;


/**
 * Extension to start up the ColumnTemplate and Repository
 * from the {@link org.eclipse.jnosql.mapping.Database} qualifier
 */
public class ColumnExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(ColumnExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();


    <T, X extends ColumnManager> void observes(@Observes final ProcessProducer<T, X> pp) {
        Databases.addDatabase(pp, COLUMN, databases);
    }

    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery) {

        ClassScanner scanner = ClassScanner.INSTANCE;
        Set<Class<?>> crudTypes = scanner.repositoriesStandard();
        LOGGER.info(String.format("Processing Column extension: %d databases crud %d found",
                databases.size(), crudTypes.size()));
        LOGGER.info("Processing repositories as a Column implementation: " + crudTypes);

        databases.forEach(type -> {
            final TemplateBean bean = new TemplateBean(type.getProvider());
            afterBeanDiscovery.addBean(bean);
        });

        crudTypes.forEach(type -> {
            if (!databases.contains(DatabaseMetadata.DEFAULT_COLUMN)) {
                afterBeanDiscovery.addBean(new RepositoryColumnBean(type, ""));
            }
            databases.forEach(database -> afterBeanDiscovery
                    .addBean(new RepositoryColumnBean(type, database.getProvider())));
        });

    }

}
