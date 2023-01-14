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
package org.eclipse.jnosql.mapping.keyvalue.spi;


import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.communication.keyvalue.BucketManager;
import org.eclipse.jnosql.mapping.DatabaseMetadata;
import org.eclipse.jnosql.mapping.Databases;
import org.eclipse.jnosql.mapping.keyvalue.query.RepositoryKeyValueBean;

import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.spi.AfterBeanDiscovery;
import jakarta.enterprise.inject.spi.Extension;
import jakarta.enterprise.inject.spi.ProcessAnnotatedType;
import jakarta.enterprise.inject.spi.ProcessProducer;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static org.eclipse.jnosql.mapping.DatabaseType.KEY_VALUE;


public class KeyValueExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(KeyValueExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();

    private final Collection<Class<?>> crudTypes = new HashSet<>();

    <T, X extends BucketManager> void observes(@Observes final ProcessProducer<T, X> pp) {
        Databases.addDatabase(pp, KEY_VALUE, databases);
    }

    <T extends CrudRepository> void observes(@Observes final ProcessAnnotatedType<T> type) {
        Class<T> javaClass = type.getAnnotatedType().getJavaClass();

        if (CrudRepository.class.equals(javaClass) || PageableRepository.class.equals(javaClass)) {
            return;
        }

        if (isRepositoryType(javaClass)
                && Modifier.isInterface(javaClass.getModifiers())) {
            crudTypes.add(type.getAnnotatedType().getJavaClass());
        }
    }

    private <T> boolean isRepositoryType(Class<T> type) {
        return Arrays.asList(type.getInterfaces()).contains(CrudRepository.class)
                ||
                Arrays.asList(type.getInterfaces()).contains(PageableRepository.class);
    }

    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery) {
        LOGGER.info(String.format("Processing Key-Value extension: %d databases crud %d found",
                databases.size(), crudTypes.size()));
        LOGGER.info("Processing repositories as a Key-Value implementation: " + crudTypes);

        databases.forEach(type -> {
            final TemplateBean bean = new TemplateBean(type.getProvider());
            afterBeanDiscovery.addBean(bean);
        });

        crudTypes.forEach(type -> {

            if (!databases.contains(DatabaseMetadata.DEFAULT_KEY_VALUE)) {
                afterBeanDiscovery.addBean(new RepositoryKeyValueBean(type, ""));
            }

            databases.forEach(database -> afterBeanDiscovery
                    .addBean(new RepositoryKeyValueBean(type, database.getProvider())));
        });

    }

}
