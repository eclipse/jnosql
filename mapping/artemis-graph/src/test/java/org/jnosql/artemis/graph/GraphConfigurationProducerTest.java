/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.jnosql.artemis.graph.producer.GraphMockA;
import org.jnosql.artemis.graph.producer.GraphMockB;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;


@ExtendWith(CDIExtension.class)
class GraphConfigurationProducerTest {

    @Inject
    @ConfigurationUnit(fileName = "graph.json", name = "graphA")
    private Graph graphA;

    @Inject
    @ConfigurationUnit(fileName = "graph.json", name = "graphB")
    private Graph graphB;


    @Test
    public void shouldInjectGraphA() {
        Assertions.assertTrue(graphA instanceof GraphMockA);
        GraphMockA graphmock = (GraphMockA) graphA;
        Assertions.assertEquals("valueA", graphmock.get("key"));
        Assertions.assertEquals("value2A", graphmock.get("key2"));

    }

    @Test
    public void shouldInjectGraphB() {
        Assertions.assertTrue(graphB instanceof GraphMockB);
        GraphMockB graphmock = (GraphMockB) graphB;
        Assertions.assertEquals("valueB", graphmock.get("key"));
        Assertions.assertEquals("value2B", graphmock.get("key2"));

    }
}