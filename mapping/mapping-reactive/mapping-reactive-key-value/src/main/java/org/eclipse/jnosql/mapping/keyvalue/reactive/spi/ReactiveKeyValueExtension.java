/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.keyvalue.reactive.spi;


import jakarta.nosql.keyvalue.BucketManager;
import org.eclipse.jnosql.mapping.DatabaseMetadata;
import org.eclipse.jnosql.mapping.Databases;
import org.eclipse.jnosql.mapping.keyvalue.reactive.query.ReactiveRepositoryKeyValueBean;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.BeanManager;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessProducer;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static jakarta.nosql.mapping.DatabaseType.KEY_VALUE;

/**
 * Extension to start up {@link jakarta.nosql.mapping.keyvalue.KeyValueTemplate} and {@link ReactiveRepository}
 * from the {@link jakarta.enterprise.inject.Default} and {@link jakarta.nosql.mapping.Database} qualifier
 */
public class ReactiveKeyValueExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(ReactiveKeyValueExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();

    private final Collection<Class<?>> crudTypes = new HashSet<>();

    <T, X extends BucketManager> void observes(@Observes final ProcessProducer<T, X> pp) {
        Databases.addDatabase(pp, KEY_VALUE, databases);
    }

    <T extends ReactiveRepository<?, ?>> void observes(@Observes final ProcessAnnotatedType<T> repo) {
        Class<T> javaClass = repo.getAnnotatedType().getJavaClass();

        if (ReactiveRepository.class.equals(javaClass)) {
            return;
        }

        if (Arrays.asList(javaClass.getInterfaces()).contains(ReactiveRepository.class)
                && Modifier.isInterface(javaClass.getModifiers())) {
            crudTypes.add(repo.getAnnotatedType().getJavaClass());
        }
    }

    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery, final BeanManager beanManager) {
        LOGGER.info(String.format("Processing reactive Key-Value extension: %d databases crud %d found",
                databases.size(), crudTypes.size()));
        LOGGER.info("Processing repositories as a reactive Key-Value implementation: " + crudTypes);
        databases.forEach(type -> {
            final ReactiveTemplateBean bean = new ReactiveTemplateBean(beanManager, type.getProvider());
            afterBeanDiscovery.addBean(bean);
        });

        crudTypes.forEach(type -> {

            if (!databases.contains(DatabaseMetadata.DEFAULT_KEY_VALUE)) {
                afterBeanDiscovery.addBean(new ReactiveRepositoryKeyValueBean(type, beanManager, ""));
            }

            databases.forEach(database -> afterBeanDiscovery
                    .addBean(new ReactiveRepositoryKeyValueBean(type, beanManager, database.getProvider())));
        });

    }

}
