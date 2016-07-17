package org.apache.diana.api.column;

import org.apache.diana.api.document.Document;
import org.apache.diana.api.util.DefaultValue;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Columns {

    private Columns() {}

    public static Column of(String name, Object value) {
        return Column.of(name, DefaultValue.of(value));
    }

    public static List<Column> of(Map<String, Objects> values) {
        Predicate<String> isNotNull = s -> values.get(s) != null;
        Function<String, Column> documentMap = key -> {
            Objects value = values.get(key);
            return Column.of(key, DefaultValue.of(value));
        };
        return values.keySet().stream().filter(isNotNull).map(documentMap).collect(Collectors.toList());
    }
}
