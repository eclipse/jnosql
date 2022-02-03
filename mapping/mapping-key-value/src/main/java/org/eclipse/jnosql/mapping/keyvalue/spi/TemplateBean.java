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
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplateProducer;
import org.eclipse.jnosql.mapping.DatabaseQualifier;
import org.eclipse.jnosql.mapping.spi.AbstractBean;

import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

class TemplateBean extends AbstractBean<KeyValueTemplate> {

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public TemplateBean(BeanManager beanManager, String provider) {
        super(beanManager);
        this.types = Collections.singleton(KeyValueTemplate.class);
        this.provider = provider;
        this.qualifiers = Collections.singleton(DatabaseQualifier.ofKeyValue(provider));
    }

    @Override
    public Class<?> getBeanClass() {
        return KeyValueTemplate.class;
    }

    @Override
    public KeyValueTemplate create(CreationalContext<KeyValueTemplate> creationalContext) {

        KeyValueTemplateProducer producer = getInstance(KeyValueTemplateProducer.class);
        BucketManager manager = getManager();
        return producer.get(manager);
    }

    private BucketManager getManager() {
        return getInstance(BucketManager.class, DatabaseQualifier.ofKeyValue(provider));
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
        return KeyValueTemplate.class.getName() + DatabaseType.KEY_VALUE + "-" + provider;
    }

}
