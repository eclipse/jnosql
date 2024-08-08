/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import jakarta.nosql.AttributeConverter;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.mapping.metadata.MapParameterMetaData;
import org.eclipse.jnosql.mapping.metadata.MappingType;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

class DefaultMapParameterMetaData extends DefaultParameterMetaData implements MapParameterMetaData {


    private final TypeSupplier<?> typeSupplier;
    private final Class<?> keyType;
    private final Class<?> valueType;

    DefaultMapParameterMetaData(String name, Class<?> type, boolean id,
                                Class<? extends AttributeConverter<?, ?>> converter,
                                MappingType mappingType, TypeSupplier<?> typeSupplier) {
        super(name, type, id, converter, mappingType);
        this.typeSupplier = typeSupplier;
        this.keyType = (Class<?>) ((ParameterizedType) typeSupplier.get()).getActualTypeArguments()[0];
        this.valueType = (Class<?>) ((ParameterizedType) typeSupplier.get()).getActualTypeArguments()[1];
    }

    public Class<?> elementType() {
        return (Class<?>) ((ParameterizedType) typeSupplier.get()).getActualTypeArguments()[0];
    }


    @Override
    public Class<?> keyType() {
       return this.keyType;
    }

    @Override
    public Class<?> valueType() {
        return this.valueType;
    }

    @Override
    public Map<?, ?> mapInstance() {
        return new HashMap<>();
    }

}
