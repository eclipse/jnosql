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


import org.jnosql.artemis.DatabaseQualifier;
import jakarta.nosql.mapping.DatabaseType;
import org.jnosql.artemis.key.KeyValueTemplate;
import org.jnosql.artemis.key.KeyValueTemplateProducer;
import org.jnosql.artemis.spi.AbstractBean;
import org.jnosql.diana.key.BucketManager;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
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
        Bean<BucketManager> bean = (Bean<BucketManager>) getBeanManager().getBeans(BucketManager.class,
                DatabaseQualifier.ofKeyValue(provider) ).iterator().next();
        CreationalContext<BucketManager> ctx = getBeanManager().createCreationalContext(bean);
        return (BucketManager) getBeanManager().getReference(bean, BucketManager.class, ctx);
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
        return KeyValueTemplate.class.getName() + DatabaseType.COLUMN + "-" + provider;
    }

}
