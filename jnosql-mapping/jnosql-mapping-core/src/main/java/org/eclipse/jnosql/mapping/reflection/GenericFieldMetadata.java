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


import jakarta.nosql.Entity;
import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.mapping.AttributeConverter;
import org.eclipse.jnosql.mapping.Embeddable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.ServiceLoader;

public class GenericFieldMetadata extends AbstractFieldMetadata {

    private final TypeSupplier<?> typeSupplier;

    GenericFieldMetadata(MappingType type, Field field, String name, TypeSupplier<?> typeSupplier,
                         Class<? extends AttributeConverter<?, ?>> converter, FieldReader reader, FieldWriter writer) {
        super(type, field, name, converter, reader, writer);
        this.typeSupplier = typeSupplier;
    }

    @Override
    public Object value(Value value) {
        if(value.get() instanceof Iterable) {
            return value.get(typeSupplier);
        } else {
            return Value.of(Collections.singletonList(value.get())).get(typeSupplier);
        }
    }

    @Override
    public boolean isId() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenericFieldMetadata that = (GenericFieldMetadata) o;
        return type == that.type &&
                Objects.equals(field, that.field) &&
                Objects.equals(typeSupplier, that.typeSupplier) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, field, name, typeSupplier);
    }

    public boolean isEmbeddable() {
        return isEmbeddableField() || isEntityField();
    }

    private boolean isEntityField() {
        return hasFieldAnnotation(Entity.class);
    }

    private boolean isEmbeddableField() {
        return hasFieldAnnotation(Embeddable.class);
    }

    private boolean hasFieldAnnotation(Class<?> annotation) {
        return ((Class) ((ParameterizedType) nativeField()
                .getGenericType())
                .getActualTypeArguments()[0])
                .getAnnotation(annotation) != null;
    }

    public Class<?> getElementType() {
        return (Class) ((ParameterizedType) nativeField()
                .getGenericType())
                .getActualTypeArguments()[0];
    }

    public Collection<?> getCollectionInstance() {
        Class<?> type = nativeField().getType();
        final CollectionSupplier supplier =  ServiceLoader.load(CollectionSupplier.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .map(CollectionSupplier.class::cast)
                .filter(c -> c.test(type))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("This collection is not supported yet: " + type));
        return (Collection<?>) supplier.get();
    }

    @Override
    public String toString() {
        return "GenericFieldMapping{" + "typeSupplier=" + typeSupplier +
                ", type=" + type +
                ", field=" + field +
                ", name='" + name + '\'' +
                ", fieldName='" + fieldName + '\'' +
                ", converter=" + converter +
                '}';
    }
}
