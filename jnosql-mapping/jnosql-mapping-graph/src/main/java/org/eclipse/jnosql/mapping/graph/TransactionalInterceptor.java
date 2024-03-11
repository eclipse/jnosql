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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;

import jakarta.annotation.Priority;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.jnosql.communication.graph.GraphTransactionUtil;


@Transactional
@Interceptor
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
class TransactionalInterceptor {

    @Inject
    private Instance<Graph> graph;

    @AroundInvoke
    public Object manageTransaction(InvocationContext context) throws Exception {
        Transaction transaction = graph.get().tx();
        GraphTransactionUtil.lock(transaction);
        if (!transaction.isOpen()) {
            transaction.open();
        }
        try {
            Object proceed = context.proceed();
            transaction.commit();
            return proceed;
        } catch (Exception exception) {
            transaction.rollback();
            throw exception;
        }finally {
            GraphTransactionUtil.unlock();
        }

    }


}
