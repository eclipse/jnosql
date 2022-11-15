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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.eclipse.jnosql.mapping.config.MappingConfigurations.GRAPH_TRANSACTION_AUTOMATIC;

class GraphTransactionUtilTest {

    @Test
    public void shouldReturnTrue() {
        Assertions.assertTrue(GraphTransactionUtil.isAutomatic());
    }

    @Test
    public void shouldReturnFalse() {
        synchronized (GraphTransactionUtil.class) {
            System.setProperty(GRAPH_TRANSACTION_AUTOMATIC.get(), Boolean.FALSE.toString());
            Assertions.assertFalse(GraphTransactionUtil.isAutomatic());
            System.clearProperty(GRAPH_TRANSACTION_AUTOMATIC.get());
        }
    }

}