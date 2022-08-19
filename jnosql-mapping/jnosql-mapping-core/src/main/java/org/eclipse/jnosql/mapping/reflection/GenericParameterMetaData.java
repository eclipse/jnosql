/*
 *  Copyright (c) 2022 Otávio Santana and others
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

import jakarta.nosql.TypeSupplier;
import jakarta.nosql.mapping.AttributeConverter;

public final class GenericParameterMetaData extends DefaultParameterMetaData implements ParameterMetaData {

    private final TypeSupplier<?> typeSupplier;

    GenericParameterMetaData(String name, Class<?> type, boolean id,
                             Class<? extends AttributeConverter<?, ?>> converter,
                             MappingType mappingType, TypeSupplier<?> typeSupplier) {
        super(name, type, id, converter, mappingType);
        this.typeSupplier = typeSupplier;
    }

    public TypeSupplier<?> getTypeSupplier() {
        return typeSupplier;
    }

}
