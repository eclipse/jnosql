/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.writer;

import org.jnosql.diana.api.ValueWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Decorators of all {@link ValueWriter} supported by Diana
 * @see ValueWriter
 */
@SuppressWarnings("unchecked")
public final class ValueWriterDecorator implements ValueWriter {

    private static final ValueWriter INSTANCE = new ValueWriterDecorator();

    private final List<ValueWriter> writers = new ArrayList<>();

    {
        ServiceLoader.load(ValueWriter.class).forEach(writers::add);
    }

    private ValueWriterDecorator() {
    }

    public static ValueWriter getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isCompatible(Class clazz) {
        return writers.stream().anyMatch(writerField -> writerField.isCompatible(clazz));
    }

    @Override
    public Object write(Object object) {
        Class clazz = object.getClass();
        ValueWriter valueWriter = writers.stream().filter(r -> r.isCompatible(clazz)).findFirst().orElseThrow(
            () -> new UnsupportedOperationException("The type " + clazz + " is not supported yet"));
        return valueWriter.write(object);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ValueWriterDecorator{");
        sb.append("writers=").append(writers);
        sb.append('}');
        return sb.toString();
    }
}
