package org.jnosql.artemis.column;

import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination.ColumnQueryPaginationProvider;

import java.util.Objects;

public final class DefaultColumnQueryPaginationProvider implements ColumnQueryPaginationProvider {

    @Override
    public ColumnQueryPagination apply(ColumnQuery columnQuery, Pagination pagination) {
        Objects.requireNonNull(columnQuery, "columnQuery is requried");
        Objects.requireNonNull(pagination, "pagination is requried");
        return new DefaultColumnQueryPagination(columnQuery, pagination);
    }
}
