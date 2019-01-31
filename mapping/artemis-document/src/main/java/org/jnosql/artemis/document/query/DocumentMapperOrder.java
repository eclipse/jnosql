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
package org.jnosql.artemis.document.query;


/**
 * The Document Order whose define the sort in the query.
 */
public interface DocumentMapperOrder {

    /**
     * Defines the order as {@link org.jnosql.diana.api.Sort.SortType#ASC}
     * @return the {@link DocumentMapperNameOrder} instance
     */
    DocumentMapperNameOrder asc();

    /**
     * Defines the order as {@link org.jnosql.diana.api.Sort.SortType#DESC}
     * @return the {@link DocumentMapperNameOrder} instance
     */
    DocumentMapperNameOrder desc();
}
