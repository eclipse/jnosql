/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link WriterField} supported by Diana
 *
 * @author Otávio Santana
 * @see WriterField
 */
final class WriterFieldDecorator implements WriterField {

    private static WriterField INSTANCE = new WriterFieldDecorator();

    private final List<WriterField> writers = new ArrayList<>();

    {
        ServiceLoader.load(WriterField.class).forEach(writers::add);
    }

    public static WriterField getInstance() {
        return INSTANCE;
    }

    private WriterFieldDecorator() {
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return writers.stream().anyMatch(writerField -> writerField.isCompatible(clazz));
    }

    @Override
    public Object write(Object object) {
        Class clazz = object.getClass();
        WriterField writerField = writers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
                () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return writerField.write(object);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WriterFieldDecorator{");
        sb.append("writers=").append(writers);
        sb.append('}');
        return sb.toString();
    }
}
