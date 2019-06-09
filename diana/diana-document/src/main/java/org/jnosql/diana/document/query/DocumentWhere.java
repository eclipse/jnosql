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


import org.jnosql.diana.document.DocumentCondition;

/**
 * The Document Where whose define the condition in the query.
 */
public interface DocumentWhere extends DocumentQueryBuild {


    /**
     * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link DocumentNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    DocumentNameCondition and(String name);

    /**
     * Appends a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link DocumentNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    DocumentNameCondition or(String name);

    /**
     * Defines the position of the first result to retrieve.
     *
     * @param skip the first result to retrive
     * @return a query with first result defined
     */
    DocumentSkip skip(long skip);


    /**
     * Defines the maximum number of results to retrieve.
     *
     * @param limit the limit
     * @return a query with the limit defined
     */
    DocumentLimit limit(long limit);

    /**
     * Add the order how the result will returned
     *
     * @param name the order
     * @return a query with the sort defined
     * @throws NullPointerException when name is null
     */
    DocumentOrder orderBy(String name);

}
