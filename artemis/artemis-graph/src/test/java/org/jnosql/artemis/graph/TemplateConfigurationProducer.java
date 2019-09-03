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
package org.jnosql.artemis.graph;


import jakarta.nosql.mapping.ConfigurationUnit;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.jnosql.artemis.graph.cdi.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

@ExtendWith(CDIExtension.class)
public class TemplateConfigurationProducer {


    @Inject
    @ConfigurationUnit(fileName = "graph.json", name = "graphA", database = "database")
    private GraphTemplate templateA;

    @Inject
    @ConfigurationUnit(fileName = "graph.json", name = "graphB", database = "database")
    private GraphTemplate templateB;



    @Test
    public void shouldTemplate() {
        Assertions.assertNotNull(templateA);
        Graph graph = ((AbstractGraphTemplate) templateA).getGraph();
        Assertions.assertNotNull(graph);
    }

    @Test
    public void shouldTemplateB() {
        Assertions.assertNotNull(templateB);
        Graph graph = ((AbstractGraphTemplate) templateB).getGraph();
        Assertions.assertNotNull(graph);
    }
}
