/*
 *
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
 *
 */
package org.jnosql.artemis;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A page is a fixed-length contiguous block of entities from the database.
 *
 * @param <T> the entity type
 */
public interface Page<T> {

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();

    /**
     * Returns the {@link Page} requesting the next {@link Page}.
     *
     * @return the next {@link Page}
     */
    Page<T> next();

    /**
     * Returns the page content as {@link List}
     *
     * @return the content as {@link List}
     */
    List<T> getContent();

    /**
     * Returns the page content as from collection Factory
     *
     * @param collectionFactory the collectionFactory
     * @param <C>               the collection type
     * @return a content of this page as {@link Collection} from collectionFactory
     */
    <C extends Collection<T>> C getContent(Supplier<C> collectionFactory);

    /**
     * Returns the page content as {@link Stream}
     *
     * @return the content as {@link Stream}
     */
    Stream<T> stream();

}
