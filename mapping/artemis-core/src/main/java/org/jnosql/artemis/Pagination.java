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

/**
 * Pagination is the process of separating print or digital content into discrete pages.
 * This instance represents this pagination process.
 */
public interface Pagination {

    /**
     * Returns the page to be returned.
     *
     * @return the page to be returned.
     */
    long getPageNumber();

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page
     */
    long getPageSize();

    /**
     * @return The maximum number of results the select object was set to retrieve.
     * According to the underlying page and page size.
     */
    long getLimit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * According to the underlying page and page size.
     */
    long getSkip();


    /**
     * Returns the {@link Pagination} requesting the next {@link Pagination}.
     *
     * @return the next pagination
     */
    Pagination next();

    /**
     * Returns a pagination instance that is read-only, in other words, that is not allowed to use the {@link Pagination#next()}.
     *
     * @return a read-onlye {@link Pagination} instance
     */
    Pagination unmodifiable();

    /**
     * A builder to {@link Pagination}, as the first step it defines the page number or the page index that starts from page one.
     *
     * @param page the page index
     * @return a new {@link PaginationBuilder} instance
     * @throws IllegalArgumentException when page is lesser equals than zero
     */
    static PaginationBuilder page(long page) {
        if (page <= 0) {
            throw new IllegalArgumentException("The page index cannot be less equals than zero.");
        }
        return new PaginationBuilder(page);
    }
}
