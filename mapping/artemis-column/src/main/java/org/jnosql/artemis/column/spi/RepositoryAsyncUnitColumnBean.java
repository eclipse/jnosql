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
package org.jnosql.artemis.column.spi;

import org.jnosql.artemis.Repository;
import org.jnosql.artemis.column.ColumnRepositoryProducer;
import org.jnosql.artemis.spi.AbstractBean;
import org.jnosql.artemis.util.RepositoryUnit;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerFactory;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class RepositoryUnitColumnBean  extends AbstractBean<Repository<?, ?>> {

    private final Set<Type> types;

    private final Set<Annotation> qualifiers;

    private final RepositoryUnit repositoryUnit;

    RepositoryUnitColumnBean(BeanManager beanManager, RepositoryUnit repositoryUnit) {
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
    public Repository<?, ?> create(CreationalContext<Repository<?, ?>> context) {
        return get();
    }

    private <T, K, R extends Repository<T, K>> R get() {
        ColumnRepositoryProducer producer = getInstance(ColumnRepositoryProducer.class);
        ColumnConfigurationProducer configurationProducer = getInstance(ColumnConfigurationProducer.class);
        Class<R> repository  = (Class<R>) repositoryUnit.getRepository();
        ColumnFamilyManagerFactory<ColumnFamilyManager> managerFactory = configurationProducer.getFactory(repositoryUnit.getUnit());
        ColumnFamilyManager manager = managerFactory.get(repositoryUnit.getDatabase());
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
        return "document: " + repositoryUnit.toString();
    }
}
