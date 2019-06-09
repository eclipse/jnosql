/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis;

import org.jnosql.diana.Sort;

import java.util.List;

/**
 * This instance represents a group of one or more {@link org.jnosql.query.Sort} instances.
 * It is useful to either define or append sort from a field at a {@link Repository} within a parameter to query by a method.
 */
public interface Sorts {

    /**
     * Appends a {@link Sort} instance of {@link Sort.SortType#ASC}
     * from the name
     *
     * @param name the name
     * @return a {@link Sorts} instance
     * @throws NullPointerException when name is not null
     */
    Sorts asc(String name);

    /**
     * Appends a {@link Sort} instance of {@link Sort.SortType#DESC}
     * from the name
     *
     * @param name the name
     * @return a {@link Sorts} instance
     * @throws NullPointerException when name is not null
     */
    Sorts desc(String name);

    /**
     * Appends a {@link Sort} instance
     *
     * @param sort the sort
     * @return a {@link Sorts} instance
     * @throws NullPointerException when sort is null
     */
    Sorts add(Sort sort);

    /**
     * Removes a {@link Sort} at {@link Sorts}
     *
     * @param sort the sort to be removed
     * @return a {@link Sorts} instance
     * @throws NullPointerException when sort is null
     */
    Sorts remove(Sort sort);

    /**
     * @return a list of {@link Sort} within in {@link Sorts}
     */
    List<Sort> getSorts();


    /**
     * Creates a new instance of {@link Sorts}
     *
     * @return a {@link Sorts} instance
     */
    static Sorts sorts() {
        return new DefaultSorts();
    }


}
