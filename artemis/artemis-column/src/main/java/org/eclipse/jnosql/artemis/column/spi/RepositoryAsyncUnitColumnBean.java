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
package org.eclipse.jnosql.artemis.column.spi;

import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnFamilyManagerAsyncFactory;
import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnRepositoryAsyncProducer;
import org.eclipse.jnosql.artemis.spi.AbstractBean;
import org.eclipse.jnosql.artemis.util.RepositoryUnit;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class RepositoryAsyncUnitColumnBean extends AbstractBean<RepositoryAsync<?, ?>> {

    private final Set<Type> types;

    private final Set<Annotation> qualifiers;

    private final RepositoryUnit repositoryUnit;

    RepositoryAsyncUnitColumnBean(BeanManager beanManager, RepositoryUnit repositoryUnit) {
        super(beanManager);
        this.types = Collections.singleton(repositoryUnit.getRepository());
        this.qualifiers = Collections.singleton(repositoryUnit.getUnit());
        this.repositoryUnit = repositoryUnit;
    }


    @Override
    public Class<?> getBeanClass() {
        return repositoryUnit.getRepository();
    }

    @Override
    public RepositoryAsync<?, ?> create(CreationalContext<RepositoryAsync<?, ?>> context) {
        return get();
    }

    private <T, K, R extends RepositoryAsync<T, K>> R get() {
        ColumnRepositoryAsyncProducer producer = getInstance(ColumnRepositoryAsyncProducer.class);
        ColumnConfigurationProducer configurationProducer = getInstance(ColumnConfigurationProducer.class);
        Class<R> repository  = (Class<R>) repositoryUnit.getRepository();
        ColumnFamilyManagerAsyncFactory managerFactory = configurationProducer.getFactoryAsync(repositoryUnit.getUnit());
        ColumnFamilyManagerAsync manager = managerFactory.getAsync(repositoryUnit.getDatabase());
        return producer.get(repository, manager);
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return qualifiers;
    }

    @Override
    public String getId() {
        return "column-async: " + repositoryUnit.toString();
    }
}
