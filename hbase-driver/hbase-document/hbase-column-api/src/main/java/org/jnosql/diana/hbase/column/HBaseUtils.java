package org.jnosql.diana.hbase.column;


import org.jnosql.diana.api.column.Column;

final class HBaseUtils {

    static final String KEY_COLUMN = "";


    static Column getKey(Object value) {
        return Column.of(KEY_COLUMN, value);
    }

    private HBaseUtils() {}
}
