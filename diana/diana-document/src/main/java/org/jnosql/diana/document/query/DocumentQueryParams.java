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
package org.jnosql.diana.document.query;

import org.jnosql.diana.Params;
import org.jnosql.diana.document.DocumentQuery;

/**
 * The result of {@link SelectQueryConverter} that has {@link DocumentQuery} and {@link Params}.
 */
public interface DocumentQueryParams {

    /**
     * The {@link DocumentQuery}
     *
     * @return a {@link DocumentQuery} instance
     */
    DocumentQuery getQuery();

    /**
     * The {@link Params}
     *
     * @return a {@link Params} instance
     */
    Params getParams();
}
