/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.reader;

import org.eclipse.jnosql.communication.TypeReferenceReader;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.ValueReader;
import org.eclipse.jnosql.communication.ValueReaderDecorator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * The {@link TypeReferenceReader} to {@link java.util.Optional}
 */
@SuppressWarnings("unchecked")
public class OptionalTypeReferenceReader implements TypeReferenceReader {

    private static final ValueReader SERVICE_PROVIDER = ValueReaderDecorator.getInstance();

    @Override
    public boolean test(TypeSupplier<?> typeReference) {
        Type type = typeReference.get();
        if (type instanceof ParameterizedType parameterizedType) {

            return Optional.class.equals(parameterizedType.getRawType()) &&
                    Class.class.isInstance(parameterizedType.getActualTypeArguments()[0]);
        }
        return false;
    }

    @Override
    public <T> T convert(TypeSupplier<T> typeReference, Object value) {

        Type type = typeReference.get();
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class<?> classType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
        return (T) Optional.ofNullable(SERVICE_PROVIDER.read(classType, value));
    }
}
