/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.communication.semistructured.DatabaseManager;

import java.util.function.Supplier;

/**
 * A specialized extension of {@link DatabaseManager} that provides access to a graph database represented by the
 * {@link org.apache.tinkerpop.gremlin.structure.Graph} interface from Apache TinkerPop.
 * <p>
 * Implementations of this interface are expected to provide methods for interacting with the underlying graph
 * database, such as retrieving vertices, edges, and properties, executing graph traversals, and performing other
 * graph-related operations.
 * <p>
 * In addition to the functionality inherited from {@link DatabaseManager}, implementations of this interface
 * also act as suppliers of the underlying {@link org.apache.tinkerpop.gremlin.structure.Graph} instance.
 * </p>
 */
public interface GraphDatabaseManager extends DatabaseManager, Supplier<Graph> {
}
