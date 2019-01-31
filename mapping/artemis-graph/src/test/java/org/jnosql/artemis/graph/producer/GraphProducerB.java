/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.jnosql.artemis.graph.producer;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.jnosql.artemis.graph.GraphProducer;
import org.jnosql.diana.api.Settings;

public class GraphProducerB implements GraphProducer {


    @Override
    public Graph apply(Settings settings) {
        return new GraphMockB(settings);
    }
}
