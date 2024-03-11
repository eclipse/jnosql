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
package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.io.File;
import java.util.function.Supplier;
import java.util.logging.Logger;

import static java.lang.System.currentTimeMillis;

public enum GraphSupplier implements Supplier<Graph> {
    INSTANCE;

    private static final Logger LOGGER = Logger.getLogger(GraphSupplier.class.getName());

    private final String directory;

    private final Graph graph;

    {
        this.directory = new File("").getAbsolutePath() + "/target/jnosql-communication-graph/" + currentTimeMillis() + "/";
        graph = Neo4jGraph.open(directory);
    }

    @Override
    public Graph get() {
        LOGGER.info("Starting Graph database at directory: " + directory);
        return graph;
    }
}
