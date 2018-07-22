/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentQuery;

/**
 * The last step to the build of {@link org.jnosql.diana.api.document.DocumentQuery}.
 * It either can return a new {@link org.jnosql.diana.api.document.DocumentQuery} instance or execute a query with
 * {@link org.jnosql.diana.api.document.DocumentCollectionManager}
 * and {@link org.jnosql.diana.api.document.DocumentCollectionManagerAsync}
 */
public interface DocumentQueryBuild {

    /**
     * Creates a new instance of {@link DocumentQuery}
     *
     * @return a new {@link DocumentQuery} instance
     */
    DocumentQuery build();
}
