package org.eclipse.jnosql.mapping.graph;

import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;
import org.eclipse.jnosql.mapping.semistructured.AbstractSemistructuredTemplate;

abstract class AbstractGraphTemplate extends AbstractSemistructuredTemplate implements GraphTemplate {

    protected abstract GraphDatabaseManager manager();
}
