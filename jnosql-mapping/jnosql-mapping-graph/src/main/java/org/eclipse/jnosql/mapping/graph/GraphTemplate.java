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

import org.eclipse.jnosql.mapping.semistructured.SemistructuredTemplate;


/**
 * GraphTemplate is a helper class that increases productivity when performing common Graph operations.
 * Includes integrated object mapping between documents and POJOs {@link org.apache.tinkerpop.gremlin.structure.Vertex}
 * and {@link org.apache.tinkerpop.gremlin.structure.Edge}.
 * It represents the common operation between an entity and {@link org.apache.tinkerpop.gremlin.structure.Graph}
 *
 * @see org.apache.tinkerpop.gremlin.structure.Graph
 */
public interface GraphTemplate extends SemistructuredTemplate {


}
