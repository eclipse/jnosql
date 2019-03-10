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

import java.util.Objects;

/**
 * The default implementation of {@link Pagination}
 */
final class DefaultPagination implements Pagination {

    private final long page;

    private final long size;

    DefaultPagination(long page, long size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public long getPageNumber() {
        return page;
    }

    @Override
    public long getPageSize() {
        return size;
    }

    @Override
    public long getLimit() {
        return size;
    }

    @Override
    public long getSkip() {
        return size * (page - 1);
    }

    @Override
    public Pagination next() {
        return new DefaultPagination(page + 1, size);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultPagination that = (DefaultPagination) o;
        return page == that.page &&
                size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size);
    }

    @Override
    public String toString() {
        return "page " + page + " of " + size;
    }
}
