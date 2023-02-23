/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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

/**
 * This singleton has the goal to interpolate params inside the Gremlin query.
 * Thus, given the query:
 * ""g.V().hasLabel(@param)" where the params is {"param":"Otavio"}
 * It should return the query to: g.V().hasLabel("Otavio")
 * It should check the Gremlin query options:
 * https://github.com/apache/tinkerpop/blob/e1396223ea9e1d6240c1f051036cbb5507f47f8d/gremlin-language/src/main/antlr4/Gremlin.g4
 */
enum GremlinParamParser {
    INSTANCE;
}
