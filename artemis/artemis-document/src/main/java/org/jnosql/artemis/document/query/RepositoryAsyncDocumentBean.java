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
package org.jnosql.artemis.document.query;

import jakarta.nosql.mapping.Converters;
import org.jnosql.artemis.DatabaseQualifier;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.RepositoryAsync;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.artemis.spi.AbstractBean;
import org.jnosql.artemis.util.AnnotationLiteralUtil;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Artemis discoveryBean to CDI extension to register {@link org.jnosql.artemis.RepositoryAsync}
 */
public class RepositoryAsyncDocumentBean extends AbstractBean<RepositoryAsync>{

    private final Class type;

    private final Set<Type> types;

    private final String provider;

    private final Set<Annotation> qualifiers;

    /**
     * Constructor
     *
     * @param type        the tye
     * @param beanManager the beanManager
     * @param provider    the provider name, that must be a
     */
    public RepositoryAsyncDocumentBean(Class type, BeanManager beanManager, String provider) {
        super(beanManager);
        this.type = type;
        this.types = Collections.singleton(type);
        this.provider = provider;
        if (provider.isEmpty()) {
            this.qualifiers = new HashSet<>();
            qualifiers.add(DatabaseQualifier.ofDocument());
            qualifiers.add(AnnotationLiteralUtil.DEFAULT_ANNOTATION);
        } else {
            this.qualifiers = Collections.singleton(DatabaseQualifier.ofDocument(provider));
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return type;
    }

    @Override
    public RepositoryAsync create(CreationalContext<RepositoryAsync> creationalContext) {
        ClassMappings classMappings = getInstance(ClassMappings.class);
        DocumentTemplateAsync repository = provider.isEmpty() ? getInstance(DocumentTemplateAsync.class) :
                getInstance(DocumentTemplateAsync.class, DatabaseQualifier.ofDocument(provider));
        Converters converters = getInstance(Converters.class);

        DocumentRepositoryAsyncProxy handler = new DocumentRepositoryAsyncProxy(repository,
                classMappings, type, converters);
        return (RepositoryAsync) Proxy.newProxyInstance(type.getClassLoader(),
                new Class[]{type},
                handler);
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
        return type.getName() + "Async@" + DatabaseType.DOCUMENT + "-" + provider;
    }

}