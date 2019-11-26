/*
 *  Copyright (c) 2019 Otávio Santana and others
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

import org.apache.tinkerpop.gremlin.process.traversal.step.util.Tree;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.artemis.graph.cdi.CDIExtension;
import org.eclipse.jnosql.artemis.graph.model.Animal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(CDIExtension.class)
public class DefaultTreeTraversalTest {

    @Inject
    protected GraphTemplate graphTemplate;

    @Inject
    protected Graph graph;

    private Animal lion;
    private Animal zebra;
    private Animal giraffe;
    private Animal grass;

    @BeforeEach
    public void setUp() {
        graph.traversal().V().toList().forEach(Vertex::remove);
        graph.traversal().E().toList().forEach(Edge::remove);
        lion = graphTemplate.insert(new Animal("lion"));
        zebra = graphTemplate.insert(new Animal("zebra"));
        giraffe = graphTemplate.insert(new Animal("giraffe"));
        grass = graphTemplate.insert(new Animal("grass"));

        graphTemplate.edge(lion, "eats", giraffe);
        graphTemplate.edge(lion, "eats", zebra);
        graphTemplate.edge(zebra, "eats", grass);
        graphTemplate.edge(giraffe, "eats", grass);
    }

    @Test
    public void shouldCreateLeaf() {
        EntityTree tree = graphTemplate.getTraversalVertex()
                .hasLabel(Animal.class)
                .in("eats")
                .tree();

        List<Animal> animals = tree.<Animal>getLeaf()
                .collect(toList());

        assertNotNull(animals);
        assertFalse(animals.isEmpty());
        assertEquals(4, animals.size());
        assertArrayEquals(animals.toArray(new Animal[4]),
                asList(lion, lion, zebra, giraffe).toArray(new Animal[4]));

    }

    @Test
    public void shouldCreateTree2() {
        Vertex lion = graph.addVertex(T.label, "animal", "name", "lion");
        Vertex zebra = graph.addVertex(T.label, "animal", "name", "zebra");
        Vertex giraffe = graph.addVertex(T.label, "animal", "name", "giraffe");
        Vertex grass = graph.addVertex(T.label, "animal", "name", "grass");

        lion.addEdge("eats", zebra);
        lion.addEdge("eats", giraffe);
        zebra.addEdge("eats", grass);
        giraffe.addEdge("eats", grass);

        List<Tree> eats = graph.traversal().V().both("eats").tree().toList();
        Tree tree = eats.get(0);
        List<Tree<?>> treesAtDepth = tree.getTreesAtDepth(1);
        List<Object> objectsAtDepth = tree.getLeafObjects();
        System.out.println("sout");

        List<Tree> eats1 = graph.traversal().E().in("eats").tree().toList();
        Tree tree1 = eats1.get(0);
        List<Tree<?>> treesAtDepth1 = tree.getTreesAtDepth(1);
        List<Object> objectsAtDepth1 = tree.getObjectsAtDepth(1);
        System.out.println("sout");

    }
}

