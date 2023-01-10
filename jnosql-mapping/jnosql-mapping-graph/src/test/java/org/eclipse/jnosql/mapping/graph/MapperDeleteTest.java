/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

import jakarta.inject.Inject;
import jakarta.nosql.tck.test.CDIExtension;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

@CDIExtension
public class MapperDeleteTest {

    @Inject
    private GraphTemplate template;

    @Inject
    private Graph graph;

    private Person otavio;
    private Person ada;
    private Person poliana;

    @AfterEach
    public void after() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
    }

    @BeforeEach
    public void before() {
        otavio = template.insert(Person.builder().withName("Otavio")
                .withAge(35).build());
        ada = template.insert(Person.builder().withName("Ada")
                .withAge(12).build());
        poliana = template.insert(Person.builder().withName("Poliana")
                .withAge(30).build());
    }
}
