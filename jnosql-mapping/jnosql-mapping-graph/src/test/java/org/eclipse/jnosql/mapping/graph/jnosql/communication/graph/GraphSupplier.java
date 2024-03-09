package org.eclipse.jnosql.mapping.graph.jnosql.communication.graph;

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
