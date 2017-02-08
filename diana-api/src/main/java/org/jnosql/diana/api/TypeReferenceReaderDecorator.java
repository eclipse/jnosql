/*
 * Copyright 2017 Eclipse Foundation
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


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link TypeReferenceReader} supported by Diana
 *
 * @see ValueReader
 */
public final class TypeReferenceReaderDecorator implements TypeReferenceReader {

    private static final TypeReferenceReaderDecorator INSTANCE = new TypeReferenceReaderDecorator();

    private final List<TypeReferenceReader> readers = new ArrayList<>();

    {
        ServiceLoader.load(TypeReferenceReader.class).forEach(readers::add);
    }

    public static TypeReferenceReaderDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public <T> boolean isCompatible(TypeSupplier<T> type) {
        return readers.stream().anyMatch(r -> r.isCompatible(type));
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {

        TypeReferenceReader valueReader = readers.stream().filter(r -> r.isCompatible(typeReference)).findFirst().
                orElseThrow(() -> new UnsupportedOperationException("The type " + typeReference + " is not supported yet"));
        return valueReader.convert(typeReference, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TypeReferenceReaderDecorator{");
        sb.append("readers=").append(readers);
        sb.append('}');
        return sb.toString();
    }


}
