package org.jnosql.diana.document;

import jakarta.nosql.Value;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.Document.DocumentProvider;

import java.util.Objects;

/**
 * Default implementation of {@link DocumentProvider}
 */
public final class DefaultDocumentProvider implements DocumentProvider {

    @Override
    public Document apply(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");
        return new DefaultDocument(name, getValue(value));
    }

    private Value getValue(Object value) {
        if (value instanceof Value) {
            return Value.class.cast(value);
        } else {
            return Value.of(value);
        }
    }
}
