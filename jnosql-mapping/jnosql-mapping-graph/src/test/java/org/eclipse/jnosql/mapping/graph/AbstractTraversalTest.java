/*
 *
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
 *
 */
package org.eclipse.jnosql.mapping.graph;

import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.entities.Book;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class AbstractTraversalTest {

    static final String READS = "reads";

    @Inject
    protected GraphTemplate graphTemplate;

    @Inject
    protected Graph graph;


    protected Person otavio;
    protected Person poliana;
    protected Person paulo;

    protected Book shack;
    protected Book license;
    protected Book effectiveJava;

    protected EdgeEntity reads;
    protected EdgeEntity reads2;
    protected EdgeEntity reads3;

    @BeforeEach
    public void setUp() {

        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);

        otavio = graphTemplate.insert(Person.builder().withAge(27)
                .withName("Otavio").build());
        poliana = graphTemplate.insert(Person.builder().withAge(26)
                .withName("Poliana").build());
        paulo = graphTemplate.insert(Person.builder().withAge(50)
                .withName("Paulo").build());

        shack = graphTemplate.insert(Book.builder().withAge(2007).withName("The Shack").build());
        license = graphTemplate.insert(Book.builder().withAge(2013).withName("Software License").build());
        effectiveJava = graphTemplate.insert(Book.builder().withAge(2001).withName("Effective Java").build());


        reads = graphTemplate.edge(otavio, READS, effectiveJava);
        reads2 = graphTemplate.edge(poliana, READS, shack);
        reads3 = graphTemplate.edge(paulo, READS, license);

        reads.add("motivation", "hobby");
        reads.add("language", "Java");
        reads2.add("motivation", "love");
        reads3.add("motivation", "job");
    }

    @AfterEach
    public void after() {
        graphTemplate.delete(otavio.getId());
        graphTemplate.delete(poliana.getId());
        graphTemplate.delete(paulo.getId());

        graphTemplate.deleteEdge(shack.getId());
        graphTemplate.deleteEdge(license.getId());
        graphTemplate.deleteEdge(effectiveJava.getId());

        reads.delete();
        reads2.delete();
        reads3.delete();
        
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
    }
}
