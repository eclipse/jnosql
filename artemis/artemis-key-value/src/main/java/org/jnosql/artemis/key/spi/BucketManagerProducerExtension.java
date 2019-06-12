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
package org.jnosql.artemis.key.spi;


import jakarta.nosql.mapping.ConfigurationUnit;
import org.jnosql.artemis.DatabaseMetadata;
import org.jnosql.artemis.Databases;
import jakarta.nosql.mapping.Repository;
import org.jnosql.artemis.key.query.RepositoryKeyValueBean;
import org.jnosql.artemis.util.RepositoryUnit;
import org.jnosql.artemis.util.ConfigurationUnitUtils;
import org.jnosql.diana.key.BucketManager;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessInjectionPoint;
import javax.enterprise.inject.spi.ProcessProducer;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import static jakarta.nosql.mapping.DatabaseType.KEY_VALUE;

/**
 * Extension to start up {@link org.jnosql.artemis.key.KeyValueTemplate} and {@link org.jnosql.artemis.Repository}
 * from the {@link javax.enterprise.inject.Default} and {@link org.jnosql.artemis.Database} qualifier
 */
public class BucketManagerProducerExtension implements Extension {

    private static final Logger LOGGER = Logger.getLogger(BucketManagerProducerExtension.class.getName());

    private final Set<DatabaseMetadata> databases = new HashSet<>();

    private final Collection<Class<?>> crudTypes = new HashSet<>();

    private final Collection<RepositoryUnit> repositoryUnits = new HashSet<>();

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
            LOGGER.info("Adding a new KeyValueRepository as discovered on key-value: " + javaClass);
            crudTypes.add(repo.getAnnotatedType().getJavaClass());
        }
    }

    <T, R extends Repository<?, ?>> void observes(@Observes ProcessInjectionPoint<T, R> event) {

        InjectionPoint injectionPoint = event.getInjectionPoint();

        if (ConfigurationUnitUtils.hasConfigurationUnit(injectionPoint)) {

            ConfigurationUnit configurationUnit = ConfigurationUnitUtils.getConfigurationUnit(injectionPoint);
            Type type = injectionPoint.getType();
            RepositoryUnit unitRepository = RepositoryUnit.of((Class<?>) type, configurationUnit);
            if (unitRepository.isKey()) {
                LOGGER.info(String.format("Found Repository to configuration unit key to configuration name %s fileName %s database: %s repository: %s",
                        configurationUnit.name(), configurationUnit.fileName(), configurationUnit.database(), type.toString()));
                repositoryUnits.add(unitRepository);

            }
        }

    }

    void onAfterBeanDiscovery(@Observes final AfterBeanDiscovery afterBeanDiscovery, final BeanManager beanManager) {
        LOGGER.info(String.format("Processing buckets: %d databases crud %d ",
                databases.size(), crudTypes.size()));

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

        repositoryUnits.forEach(type -> afterBeanDiscovery.addBean(new RepositoryUnitKeyValueBean(beanManager, type)));

    }

}
