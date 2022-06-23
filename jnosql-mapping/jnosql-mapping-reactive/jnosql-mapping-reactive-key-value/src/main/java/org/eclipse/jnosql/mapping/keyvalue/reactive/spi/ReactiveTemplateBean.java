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
package org.eclipse.jnosql.mapping.keyvalue.reactive.spi;


import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.keyvalue.reactive.ReactiveKeyValueTemplate;
import org.eclipse.jnosql.mapping.keyvalue.reactive.ReactiveKeyValueTemplateProducer;
import org.eclipse.jnosql.mapping.spi.AbstractBean;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class ReactiveTemplateBean extends AbstractBean<ReactiveKeyValueTemplate> {

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public ReactiveTemplateBean(BeanManager beanManager, String provider) {
        super(beanManager);
        this.types = Collections.singleton(ReactiveKeyValueTemplate.class);
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofKeyValue(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return ReactiveKeyValueTemplate.class;
    }

    @Override
    public ReactiveKeyValueTemplate create(CreationalContext<ReactiveKeyValueTemplate> creationalContext) {

        ReactiveKeyValueTemplateProducer producer = getInstance(ReactiveKeyValueTemplateProducer.class);
        KeyValueTemplate template = getManager();
        return producer.get(template);
    }

    private KeyValueTemplate getManager() {
        return getInstance(KeyValueTemplate.class, DatabaseQualifier.ofKeyValue(provider));
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
        return ReactiveKeyValueTemplate.class.getName() + DatabaseType.KEY_VALUE + "-" + provider;
    }

}
