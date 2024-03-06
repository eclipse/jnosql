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

    @Override
    public Graph get() {
        var directory = new File("").getAbsolutePath() + "/target/jnosql-communication-graph/" + currentTimeMillis() + "/";
        LOGGER.info("Starting Graph database at directory: " + directory);
        return Neo4jGraph.open(directory);
    }
}
