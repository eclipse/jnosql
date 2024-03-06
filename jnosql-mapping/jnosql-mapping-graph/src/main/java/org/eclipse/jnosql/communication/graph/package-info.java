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

/**
 * This package provides communication facilities and abstractions for interacting with graph databases
 * in the JNoSQL project. It contains interfaces, classes, and utilities that enable developers to
 * communicate with and manage graph databases.
 * <p>
 * The core interface in this package is {@link org.eclipse.jnosql.communication.graph.GraphDatabaseManager},
 * which extends {@link org.eclipse.jnosql.communication.semistructured.DatabaseManager}
 * and acts as a specialized extension for managing graph databases. Implementations of this interface
 * provide methods for interacting with the underlying graph database, executing graph traversals,
 * and performing other graph-related operations.
 * <p>
 * Additionally, this package may contain specific implementations, adapters, or utilities tailored
 * for working with different graph database technologies supported by JNoSQL.
 * </p>
 */
package org.eclipse.jnosql.communication.graph;