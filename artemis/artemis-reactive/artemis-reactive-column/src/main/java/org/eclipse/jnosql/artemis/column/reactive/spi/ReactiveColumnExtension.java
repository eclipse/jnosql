/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.column.reactive.spi;


import jakarta.nosql.column.ColumnFamilyManager;
import org.eclipse.jnosql.artemis.DatabaseMetadata;
import org.eclipse.jnosql.artemis.Databases;
import org.eclipse.jnosql.artemis.column.reactive.query.ReactiveRepositoryColumnBean;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessProducer;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static jakarta.nosql.mapping.DatabaseType.COLUMN;

/**
 * Extension to start up the ColumnFamily and Repository
 * from the {@link jakarta.nosql.mapping.Database} qualifier
 */
public class ReactiveColumnExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(ReactiveColumnExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();

    private final Collection<Class<?>> crudTypes = new HashSet<>();

    <T extends ReactiveRepository> void observes(@Observes final ProcessAnnotatedType<T> repo) {
        Class<T> javaClass = repo.getAnnotatedType().getJavaClass();
        if (ReactiveRepository.class.equals(javaClass)) {
            return;
        }


        if (Arrays.asList(javaClass.getInterfaces()).contains(ReactiveRepository.class)
                && Modifier.isInterface(javaClass.getModifiers())) {
            crudTypes.add(javaClass);
        }
    }


    <T, X extends ColumnFamilyManager> void observes(@Observes final ProcessProducer<T, X> pp) {
        Databases.addDatabase(pp, COLUMN, databases);
    }


    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery, final BeanManager beanManager) {
        LOGGER.info(String.format("Processing Reactive Column extension: %d databases crud %d found",
                databases.size(), crudTypes.size()));
        LOGGER.info("Processing repositories as a Reactive Column implementation: " + crudTypes.toString());

        databases.forEach(type -> {
            final ReactiveTemplateBean bean = new ReactiveTemplateBean(beanManager, type.getProvider());
            afterBeanDiscovery.addBean(bean);
        });


        crudTypes.forEach(type -> {
            if (!databases.contains(DatabaseMetadata.DEFAULT_DOCUMENT)) {
                afterBeanDiscovery.addBean(new ReactiveRepositoryColumnBean(type, beanManager, ""));
            }
            databases.forEach(database -> {
                final ReactiveRepositoryColumnBean bean = new ReactiveRepositoryColumnBean(type, beanManager, database.getProvider());
                afterBeanDiscovery.addBean(bean);
            });
        });

    }

}
