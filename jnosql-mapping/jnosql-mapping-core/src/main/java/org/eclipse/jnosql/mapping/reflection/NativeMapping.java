/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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

    private final FieldMetadata fieldMetadata;

    private NativeMapping(String nativeField, FieldMetadata fieldMetadata) {
        this.nativeField = nativeField;
        this.fieldMetadata = fieldMetadata;
    }

    public String getNativeField() {
        return nativeField;
    }

    public FieldMetadata getFieldMapping() {
        return fieldMetadata;
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
                Objects.equals(fieldMetadata, that.fieldMetadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nativeField, fieldMetadata);
    }

    @Override
    public String toString() {
        return  "NativeMapping{" + "nativeField='" + nativeField + '\'' +
                ", fieldMapping=" + fieldMetadata +
                '}';
    }

    public static NativeMapping of(String nativeField, FieldMetadata field) {
        return new NativeMapping(nativeField, field);
    }
}
