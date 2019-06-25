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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.jnosql.diana.SettingsPriority;

/**
 * An utilitarian to {@link org.apache.tinkerpop.gremlin.structure.Transaction}
 */
final class GraphTransactionUtil {

    static final String TRANSACTION_KEY = "jakarta.nosql.transaction.automatic";

    private static final ThreadLocal<Transaction> THREAD_LOCAL = new ThreadLocal<>();

    private GraphTransactionUtil() {
    }

    /**
     * Holds the current transaction to don't allow a {@link Transaction#commit()}
     *
     * @param transaction the {@link Transaction}
     */
    static void lock(Transaction transaction) {
        THREAD_LOCAL.set(transaction);
    }

    /**
     * Unlocks the {@link Transaction} of the current thread
     */
    static void unlock() {
        THREAD_LOCAL.remove();
    }

    static void transaction(Transaction transaction) {
        if (isAutomatic() && isNotHold()) {
            transaction.commit();
        }
    }

    /**
     * Check if the transaction is enable
     *
     * @return Check if the transaction is enable
     */
    static boolean isAutomatic() {
        return SettingsPriority.get(TRANSACTION_KEY)
                .map(Object::toString)
                .map(Boolean::valueOf)
                .orElse(true);
    }

    private static boolean isNotHold() {
        return THREAD_LOCAL.get() != null;
    }
}
