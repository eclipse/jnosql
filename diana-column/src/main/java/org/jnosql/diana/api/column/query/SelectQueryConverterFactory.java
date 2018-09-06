package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.query.SelectQuery;

enum SelectQueryConverterFactory implements SelectQueryConverter {

    INSTANCE;

    private final SelectQueryParser parser = new SelectQueryParser();

    @Override
    public ColumnSelectQuery apply(SelectQuery selectQuery, ColumnObserverParser columnObserverParser) {
        return parser.apply(selectQuery, columnObserverParser);
    }
}
