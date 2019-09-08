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
package org.eclipse.jnosql.artemis.graph;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.apache.tinkerpop.gremlin.structure.Graph;

import javax.enterprise.inject.Instance;
import javax.enterprise.util.TypeLiteral;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link GraphTemplateProducer}
 */
class DefaultGraphTemplateProducer implements GraphTemplateProducer {

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Inject
    private GraphEventPersistManager persistManager;

    @Override
    public GraphTemplate get(Graph graph) {
        requireNonNull(graph, "graph is required");

        SingleInstance<Graph> instance = new SingleInstance<>(graph);

        GraphConverter converter = new DefaultGraphConverter(classMappings,
                converters,
                instance);
        GraphWorkflow workflow = new DefaultGraphWorkflow(persistManager, converter);
        return new DefaultGraphTemplate(instance, classMappings, converter, workflow);
    }

    @Override
    public GraphTemplate get(GraphTraversalSourceSupplier supplier) {
        requireNonNull(supplier, "supplier is required");

        SingleInstance<GraphTraversalSourceSupplier> instance = new SingleInstance<>(supplier);

        GraphConverter converter = new DefaultGraphTraversalSourceConverter(classMappings,
                converters,
                instance);
        GraphWorkflow workflow = new DefaultGraphWorkflow(persistManager, converter);
        return new DefaultGraphTraversalSourceTemplate(instance, classMappings, converter, workflow);
    }

    static class SingleInstance<T> implements Instance<T> {

        private final T instance;

        SingleInstance(T instance) {
            this.instance = instance;
        }


        @Override
        public Instance<T> select(Annotation... annotations) {
           throw new UnsupportedOperationException("this method is not support");
        }

        @Override
        public <U extends T> Instance<U> select(Class<U> aClass, Annotation... annotations) {
            throw new UnsupportedOperationException("this method is not support");
        }

        @Override
        public <U extends T> Instance<U> select(TypeLiteral<U> typeLiteral, Annotation... annotations) {
            throw new UnsupportedOperationException("this method is not support");
        }

        @Override
        public boolean isUnsatisfied() {
            return false;
        }

        @Override
        public boolean isAmbiguous() {
            return false;
        }

        @Override
        public void destroy(T t) {
        }

        @Override
        public Iterator<T> iterator() {
         return Collections.singletonList(instance).iterator();
        }

        @Override
        public T get() {
            return instance;
        }
    }
}
