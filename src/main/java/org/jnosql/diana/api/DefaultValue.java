/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api;


import java.util.Objects;

final class DefaultValue implements Value {

    private static final transient ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    private static final transient TypeReferenceReader REFERENCE_READER = TypeReferenceReaderDecorator.getInstance();

    private final Object value;

    private DefaultValue(Object value) {
        this.value = value;
    }

    public static Value of(Object value) {
        Objects.requireNonNull(value);
        return new DefaultValue(value);
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return SERVICE_PROVIDER.read(clazz, value);
    }

    @Override
    public <T> T get(TypeSupplier<T> typeReference) throws NullPointerException, UnsupportedOperationException {
        if (REFERENCE_READER.isCompatible(Objects.requireNonNull(typeReference, "typeReference is required"))) {
            return REFERENCE_READER.convert(typeReference, value);
        }
        throw new UnsupportedOperationException("The type " + typeReference + " is not supported");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }
        Value that = (Value) o;
        return Objects.equals(value, that.get());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultValue{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
