package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.enterprise.util.AnnotationLiteral;

import java.util.Objects;


/**
 * Utilitarian class to select the {@link KeyValueDatabase}
 */
public final class KeyValueDatabaseQualifier extends AnnotationLiteral<KeyValueDatabase> implements KeyValueDatabase {

    private final String value;

    private KeyValueDatabaseQualifier(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    /**
     * Creates a {@link KeyValueDatabaseQualifier} instance
     * @param value the value
     * @return the KeyValueDatabase instance
     * @throws NullPointerException when value is null
     */
    public static KeyValueDatabase of(String value) {
        Objects.requireNonNull(value, "value is required");
        return new KeyValueDatabaseQualifier(value);
    }
}
