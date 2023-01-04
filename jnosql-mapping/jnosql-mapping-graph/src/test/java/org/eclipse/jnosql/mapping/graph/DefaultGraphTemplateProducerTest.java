/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

import org.apache.tinkerpop.gremlin.structure.Graph;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@CDIExtension
public class DefaultGraphTemplateProducerTest {

    @Inject
    private GraphTemplateProducer producer;

    @Test
    public void shouldReturnErrorWhenManagerNull() {
        assertThrows(NullPointerException.class, () -> producer.get((Graph) null));
        assertThrows(NullPointerException.class, () -> producer.get((GraphTraversalSourceSupplier) null));
    }

    @Test
    public void shouldReturnGraphTemplateWhenGetGraph() {
        Graph graph = Mockito.mock(Graph.class);
        GraphTemplate template = producer.get(graph);
        assertNotNull(template);
    }


    @Test
    public void shouldReturnGraphTemplateWhenGetGraphTraversalSourceSupplier() {
        GraphTraversalSourceSupplier supplier = Mockito.mock(GraphTraversalSourceSupplier.class);
        GraphTemplate template = producer.get(supplier);
        assertNotNull(template);
    }
}