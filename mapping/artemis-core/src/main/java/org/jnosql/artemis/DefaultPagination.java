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
package org.jnosql.artemis;


import java.util.Objects;

class DefaultPagination implements Pagination {

    private final long limit;

    private final long start;

    DefaultPagination(long limit, long start) {
        this.limit = limit;
        this.start = start;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public long getSkip() {
        return start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Pagination.class != o.getClass()) {
            return false;
        }
        Pagination that = (Pagination) o;
        return limit == that.getLimit() &&
                start == that.getSkip();
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, start);
    }

    @Override
    public String toString() {
        return  "DefaultPagination{" + "limit=" + limit +
                ", start=" + start +
                '}';
    }
}
