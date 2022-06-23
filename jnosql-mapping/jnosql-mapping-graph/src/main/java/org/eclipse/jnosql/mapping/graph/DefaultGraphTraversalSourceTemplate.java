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
package org.eclipse.jnosql.mapping.graph;

import jakarta.nosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Iterator;

/**
 * The default {@link GraphTemplate} to GraphTraversalSourceOperation
 */
@GraphTraversalSourceOperation
class DefaultGraphTraversalSourceTemplate extends AbstractGraphTemplate {


    private Instance<GraphTraversalSourceSupplier> supplierInstance;

    private ClassMappings classMappings;

    private GraphConverter converter;

    private GraphWorkflow workflow;

    private Converters converters;

    @Inject
    DefaultGraphTraversalSourceTemplate(Instance<GraphTraversalSourceSupplier> supplierInstance,
                                        ClassMappings classMappings,
                                        @GraphTraversalSourceOperation GraphConverter converter,
                                        GraphWorkflow workflow,
                                        Converters converters) {
        this.supplierInstance = supplierInstance;
        this.classMappings = classMappings;
        this.converter = converter;
        this.workflow = workflow;
        this.converters = converters;
    }

    DefaultGraphTraversalSourceTemplate() {
    }

    @Override
    protected Graph getGraph() {
        throw new UnsupportedOperationException("The GraphTraversalSourceOperation implementation does not support Graph");
    }

    @Override
    public Transaction getTransaction() {
        return getTraversal().tx();
    }

    @Override
    protected GraphTraversalSource getTraversal() {
        return supplierInstance.get().get();
    }
    @Override
    protected ClassMappings getClassMappings() {
        return classMappings;
    }

    @Override
    protected GraphConverter getConverter() {
        return converter;
    }

    @Override
    protected GraphWorkflow getFlow() {
        return workflow;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

    @Override
    protected Iterator<Vertex> getVertices(Object id) {
        return getTraversal().V(id).toList().iterator();
    }

}
