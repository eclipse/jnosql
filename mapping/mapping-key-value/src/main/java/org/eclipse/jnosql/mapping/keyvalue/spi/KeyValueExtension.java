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
package org.eclipse.jnosql.mapping.keyvalue.spi;


import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.mapping.Repository;
import org.eclipse.jnosql.mapping.DatabaseMetadata;
import org.eclipse.jnosql.mapping.Databases;
import org.eclipse.jnosql.mapping.keyvalue.query.RepositoryKeyValueBean;

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

import static jakarta.nosql.mapping.DatabaseType.KEY_VALUE;

/**
 * Extension to start up {@link jakarta.nosql.mapping.keyvalue.KeyValueTemplate} and {@link jakarta.nosql.mapping.Repository}
 * from the {@link javax.enterprise.inject.Default} and {@link jakarta.nosql.mapping.Database} qualifier
 */
public class KeyValueExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(KeyValueExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();

    private final Collection<Class<?>> crudTypes = new HashSet<>();

    <T, X extends BucketManager> void observes(@Observes final ProcessProducer<T, X> pp) {
        Databases.addDatabase(pp, KEY_VALUE, databases);
    }

    <T extends Repository> void observes(@Observes final ProcessAnnotatedType<T> repo) {
        Class<T> javaClass = repo.getAnnotatedType().getJavaClass();

        if (Repository.class.equals(javaClass)) {
            return;
        }

        if (Arrays.asList(javaClass.getInterfaces()).contains(Repository.class)
                && Modifier.isInterface(javaClass.getModifiers())) {
            crudTypes.add(repo.getAnnotatedType().getJavaClass());
        }
    }


    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery, final BeanManager beanManager) {
        LOGGER.info(String.format("Processing Key-Value extension: %d databases crud %d found",
                databases.size(), crudTypes.size()));
        LOGGER.info("Processing repositories as a Key-Value implementation: " + crudTypes.toString());

        databases.forEach(type -> {
            final TemplateBean bean = new TemplateBean(beanManager, type.getProvider());
            afterBeanDiscovery.addBean(bean);
        });

        crudTypes.forEach(type -> {

            if (!databases.contains(DatabaseMetadata.DEFAULT_KEY_VALUE)) {
                afterBeanDiscovery.addBean(new RepositoryKeyValueBean(type, beanManager, ""));
            }

            databases.forEach(database -> afterBeanDiscovery
                    .addBean(new RepositoryKeyValueBean(type, beanManager, database.getProvider())));
        });

    }

}
