/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
