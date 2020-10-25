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
package org.eclipse.jnosql.mapping.reflection;

import java.util.Objects;

final class NativeMapping {

    private final String nativeField;

    private final FieldMapping fieldMapping;

    private NativeMapping(String nativeField, FieldMapping fieldMapping) {
        this.nativeField = nativeField;
        this.fieldMapping = fieldMapping;
    }

    public String getNativeField() {
        return nativeField;
    }

    public FieldMapping getFieldMapping() {
        return fieldMapping;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NativeMapping that = (NativeMapping) o;
        return Objects.equals(nativeField, that.nativeField) &&
                Objects.equals(fieldMapping, that.fieldMapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nativeField, fieldMapping);
    }

    @Override
    public String toString() {
        return  "NativeMapping{" + "nativeField='" + nativeField + '\'' +
                ", fieldMapping=" + fieldMapping +
                '}';
    }

    public static NativeMapping of(String nativeField, FieldMapping field) {
        return new NativeMapping(nativeField, field);
    }
}
