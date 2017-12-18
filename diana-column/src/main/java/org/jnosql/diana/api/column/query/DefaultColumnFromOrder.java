package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnQuery;

import static java.util.Objects.requireNonNull;

class DefaultColumnFromOrder implements ColumnFromOrder, ColumnNameOrder {


    private String name;
    private final DefaultSelectQueryBuilder queryBuilder;

    DefaultColumnFromOrder(String name, DefaultSelectQueryBuilder queryBuilder) {
        this.name = name;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public ColumnNameOrder asc() {
        this.queryBuilder.sorts.add(Sort.of(name, Sort.SortType.ASC));
        return this;
    }

    @Override
    public ColumnNameOrder desc() {
        this.queryBuilder.sorts.add(Sort.of(name, Sort.SortType.DESC));
        return this;
    }

    @Override
    public ColumnFromOrder orderBy(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        return this;
    }

    @Override
    public ColumnStart start(long start) {
        this.queryBuilder.start(start);
        return queryBuilder;
    }

    @Override
    public ColumnLimit limit(long limit) {
        this.queryBuilder.limit(limit);
        return queryBuilder;
    }

    @Override
    public ColumnQuery build() {
        return queryBuilder.build();
    }
}
