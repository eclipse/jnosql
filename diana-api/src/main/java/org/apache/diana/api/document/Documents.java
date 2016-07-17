package org.apache.diana.api.document;

import org.apache.diana.api.DefaultValue;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Documents {

    private Documents() {}

    public static Document of(String name, Object value) {
        return Document.of(name, DefaultValue.of(value));
    }

    public static List<Document> of(Map<String, Object> values) {
        Predicate<String> isNotNull = s -> values.get(s) != null;
        Function<String, Document> documentMap = key -> {
            Object value = values.get(key);
            return Document.of(key, DefaultValue.of(value));
        };
        return values.keySet().stream().filter(isNotNull).map(documentMap).collect(Collectors.toList());
    }
}
