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


import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyMap;

class EntityMetadataBuilder {

    private String name;

    private List<String> fieldsName = Collections.emptyList();

    private Class<?> classInstance;

    private List<FieldMapping> fields = Collections.emptyList();

    private Map<String, NativeMapping> javaFieldGroupedByColumn = emptyMap();

    private Map<String, FieldMapping> fieldsGroupedByName = emptyMap();

    private InstanceSupplier instanceSupplier;

    private InheritanceMetadata inheritance;

    private boolean hasInheritanceAnnotation;

    public EntityMetadataBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EntityMetadataBuilder withFieldsName(List<String> fieldsName) {
        this.fieldsName = fieldsName;
        return this;
    }

    public EntityMetadataBuilder withClassInstance(Class<?> classInstance) {
        this.classInstance = classInstance;
        return this;
    }

    public EntityMetadataBuilder withFields(List<FieldMapping> fields) {
        this.fields = fields;
        return this;
    }

    public EntityMetadataBuilder withJavaFieldGroupedByColumn(Map<String, NativeMapping> javaFieldGroupedByColumn) {
        this.javaFieldGroupedByColumn = javaFieldGroupedByColumn;
        return this;
    }

    public EntityMetadataBuilder withFieldsGroupedByName(Map<String, FieldMapping> fieldsGroupedByName) {
        this.fieldsGroupedByName = fieldsGroupedByName;
        return this;
    }

    public EntityMetadataBuilder withInstanceSupplier(InstanceSupplier instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
        return this;
    }

    public EntityMetadataBuilder withInheritance(InheritanceMetadata inheritance) {
        this.inheritance = inheritance;
        return this;
    }

    public EntityMetadataBuilder withHasInheritanceAnnotation(boolean hasInheritanceAnnotation) {
        this.hasInheritanceAnnotation = hasInheritanceAnnotation;
        return this;
    }


    public EntityMetadata build() {
        return new DefaultEntityMetadata(name, fieldsName, classInstance, fields,
                javaFieldGroupedByColumn, fieldsGroupedByName, instanceSupplier, inheritance
                , hasInheritanceAnnotation);
    }
}