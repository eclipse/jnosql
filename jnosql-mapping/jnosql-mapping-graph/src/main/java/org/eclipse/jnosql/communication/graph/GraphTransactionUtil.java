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

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.eclipse.jnosql.mapping.core.config.MicroProfileSettings;

import java.util.Objects;
import java.util.logging.Logger;

import static org.eclipse.jnosql.mapping.core.config.MappingConfigurations.GRAPH_TRANSACTION_AUTOMATIC;

/**
 * Utility class providing methods to manage transactions in a graph database.
 * This class offers functionality to lock and unlock transactions, as well as automatic transaction management.
 */
public final class GraphTransactionUtil {

    private static final Logger LOGGER = Logger.getLogger(GraphTransactionUtil.class.getName());
    private static final ThreadLocal<Transaction> THREAD_LOCAL = new ThreadLocal<>();

    private GraphTransactionUtil() {
    }

    /**
     * Locks the current transaction, preventing it from being committed.
     *
     * @param transaction the transaction to lock
     */
    public static void lock(Transaction transaction) {
        THREAD_LOCAL.set(transaction);
    }

    /**
     * Unlocks the current transaction.
     * Allows the transaction to be committed.
     */
    public static void unlock() {
        THREAD_LOCAL.remove();
    }

    /**
     * Automatically commits a transaction if enabled and not locked.
     *
     * @param graph the graph instance
     */
    public synchronized static void transaction(Graph graph) {
        if (isAutomatic() && isNotLock() && Objects.nonNull(graph)) {
            try {
                Transaction transaction = graph.tx();
                if (transaction != null) {
                    transaction.commit();
                }
            } catch (Exception exception) {
                LOGGER.info("Unable to do transaction automatically in the graph, reason: " +
                        exception.getMessage());
            }

        }
    }

    /**
     * Checks if automatic transaction management is enabled.
     *
     * @return true if automatic transaction management is enabled, false otherwise
     */
    public static boolean isAutomatic() {
        return MicroProfileSettings.INSTANCE.get(GRAPH_TRANSACTION_AUTOMATIC, String.class)
                .map(Boolean::valueOf)
                .orElse(true);
    }

    /**
     * Checks if the current transaction is not locked.
     *
     * @return true if the current transaction is not locked, false otherwise
     */
    private static boolean isNotLock() {
        return THREAD_LOCAL.get() == null;
    }
}
