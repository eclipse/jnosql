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
package org.jnosql.artemis.graph.query;

import org.apache.tinkerpop.gremlin.structure.Graph;
import jakarta.nosql.mapping.Converters;
import org.jnosql.artemis.DatabaseQualifier;
import jakarta.nosql.mapping.DatabaseType;
import jakarta.nosql.mapping.Repository;
import org.jnosql.artemis.graph.GraphConverter;
import org.jnosql.artemis.graph.GraphTemplate;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.artemis.spi.AbstractBean;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Artemis discoveryBean to CDI extension to register {@link Repository}
 */
public class RepositoryGraphBean extends AbstractBean<Repository>{

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
    public RepositoryGraphBean(Class type, BeanManager beanManager, String provider) {
        super(beanManager);
        this.type = type;
        this.types = Collections.singleton(type);
        this.provider = provider;
        if (provider.isEmpty()) {
            this.qualifiers = new HashSet<>();
            qualifiers.add(DatabaseQualifier.ofGraph());
        } else {
            this.qualifiers = Collections.singleton(DatabaseQualifier.ofGraph(provider));
        }
    }

    @Override
    public Class<?> getBeanClass() {
        return type;
    }

    @Override
    public Repository create(CreationalContext<Repository> creationalContext) {

        ClassMappings classMappings = getInstance(ClassMappings.class);
        GraphTemplate repository = provider.isEmpty() ? getInstance(GraphTemplate.class) :
                getInstance(GraphTemplate.class, DatabaseQualifier.ofGraph(provider));
        GraphConverter converter = getInstance(GraphConverter.class);
        Graph graph = provider.isEmpty() ? getInstance(Graph.class) :
                getInstance(Graph.class, DatabaseQualifier.ofGraph(provider));
        Converters converters = getInstance(Converters.class);

        GraphRepositoryProxy handler = new GraphRepositoryProxy(repository,
                classMappings, type, graph, converter, converters);
        return (Repository) Proxy.newProxyInstance(type.getClassLoader(),
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
        return type.getName() + '@' + DatabaseType.GRAPH + "-" + provider;
    }
}
