/*
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

package org.apache.diana.api;


import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link ReaderField} supported by Diana
 *
 * @author Otávio Santana
 * @see ReaderField
 */
final class ReaderFieldDecorator implements ReaderField {

    private static final ReaderFieldDecorator INSTANCE = new ReaderFieldDecorator();

    private final List<ReaderField> readers = new ArrayList<>();

    {
        ServiceLoader.load(ReaderField.class).forEach(readers::add);
    }

    public static ReaderFieldDecorator getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return readers.stream().anyMatch(r -> r.isCompatible(clazz));
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        ReaderField readerField = readers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
            () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return readerField.read(clazz, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ReaderFieldDecorator{");
        sb.append("readers=").append(readers);
        sb.append('}');
        return sb.toString();
    }


}
