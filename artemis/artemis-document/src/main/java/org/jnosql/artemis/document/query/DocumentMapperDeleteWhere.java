/*
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
 */
package org.jnosql.artemis.document.query;


import org.jnosql.diana.document.DocumentCondition;

/**
 * The Document Where whose define the condition in the delete query.
 */
public interface DocumentMapperDeleteWhere extends DocumentMapperDeleteQueryBuild {

    /**
     * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link DocumentMapperDeleteNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    DocumentMapperDeleteNameCondition and(String name);

    /**
     * Starts a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link DocumentMapperDeleteNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    DocumentMapperDeleteNameCondition or(String name);
}
