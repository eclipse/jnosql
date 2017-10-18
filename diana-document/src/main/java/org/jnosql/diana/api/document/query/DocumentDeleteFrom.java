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


import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;

/**
 * The Document Delete Query
 */
public interface DocumentDeleteFrom {

    /**
     * Defines a new condition in the query
     *
     * @param condition the condition in the where
     * @return a new {@link DocumentDeleteWhere}
     * @throws NullPointerException when condition is null
     */
    DocumentDeleteWhere where(DocumentCondition condition) throws NullPointerException;

    /**
     * Creates a new instance of {@link DocumentDeleteQuery}
     *
     * @return a new {@link DocumentDeleteQuery} instance
     */
    DocumentDeleteQuery build();
}
