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
 * The Document Order whose define the position of the first result to retrieve.
 */
public interface DocumentSkip {

    /**
     * Defines the maximum number of results to retrieve.
     *
     * @param limit the limit
     * @return a query with the limit defined
     */
    DocumentLimit limit(long limit);

    /**
     * Creates a new instance of {@link DocumentQuery}
     *
     * @return a new {@link DocumentQuery} instance
     */
    DocumentQuery build();
}
