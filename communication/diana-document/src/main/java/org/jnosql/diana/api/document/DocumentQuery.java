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

package org.jnosql.diana.api.document;


import org.jnosql.diana.api.Sort;

import java.util.List;
import java.util.Optional;

/**
 * Class that contains information to do a select to {@link DocumentCollectionManager}
 *
 * @see DocumentCollectionManager#select(DocumentQuery)
 * @see DocumentCondition
 * @see Sort
 */
public interface DocumentQuery {


    /**
     * @return The maximum number of results the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long getSkip();


    /**
     * The document collection name
     *
     * @return the document collection name
     */
    String getDocumentCollection();

    /**
     * The conditions that contains in this {@link DocumentQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<DocumentCondition> getCondition();

    /**
     * The sorts that contains in this {@link DocumentQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> getSorts();

    /**
     * Returns the documents to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the documents
     */
    List<String> getDocuments();

}
