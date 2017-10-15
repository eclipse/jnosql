/*
 *
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
