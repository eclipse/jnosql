/*
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
 */
package org.eclipse.jnosql.mapping.reflection;

import jakarta.nosql.TypeSupplier;
import jakarta.nosql.mapping.AttributeConverter;

import java.lang.reflect.Field;

class FieldMappingBuilder {

    private FieldType type;

    private Field field;

    private String name;

    private String entityName;

    private TypeSupplier<?> typeSupplier;

    private Class<? extends AttributeConverter<?, ?>> converter;

    private boolean id;

    private FieldReader reader;

    private FieldWriter writer;


    public FieldMappingBuilder withType(FieldType type) {
        this.type = type;
        return this;
    }

    public FieldMappingBuilder withField(Field field) {
        this.field = field;
        return this;
    }

    public FieldMappingBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public FieldMappingBuilder withTypeSupplier(TypeSupplier<?> typeSupplier) {
        this.typeSupplier = typeSupplier;
        return this;
    }

    public FieldMappingBuilder withEntityName(String entityName) {
        this.entityName = entityName;
        return this;
    }

    public FieldMappingBuilder withConverter(Class<? extends AttributeConverter<?, ?>> converter) {
        this.converter = converter;
        return this;
    }

    public FieldMappingBuilder withId(boolean id) {
        this.id = id;
        return this;
    }

    public FieldMappingBuilder withWriter(FieldWriter writer) {
        this.writer = writer;
        return this;
    }

    public FieldMappingBuilder withReader(FieldReader reader) {
        this.reader = reader;
        return this;
    }

    public DefaultFieldMapping buildDefault() {
        return new DefaultFieldMapping(type, field, name, converter, id, reader, writer);
    }

    public GenericFieldMapping buildGeneric() {
        return new GenericFieldMapping(type, field, name, typeSupplier, converter, reader, writer);
    }

    public EmbeddedFieldMapping buildEmbedded() {
        return new EmbeddedFieldMapping(type, field, name, entityName, reader, writer);
    }

}
