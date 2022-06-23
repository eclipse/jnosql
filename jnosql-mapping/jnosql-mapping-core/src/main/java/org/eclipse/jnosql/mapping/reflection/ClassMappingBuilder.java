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

class ClassMappingBuilder {

    private String name;

    private List<String> fieldsName = Collections.emptyList();

    private Class<?> classInstance;

    private List<FieldMapping> fields = Collections.emptyList();

    private Map<String, NativeMapping> javaFieldGroupedByColumn = emptyMap();

    private Map<String, FieldMapping> fieldsGroupedByName = emptyMap();

    private InstanceSupplier instanceSupplier;

    public ClassMappingBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ClassMappingBuilder withFieldsName(List<String> fieldsName) {
        this.fieldsName = fieldsName;
        return this;
    }

    public ClassMappingBuilder withClassInstance(Class<?> classInstance) {
        this.classInstance = classInstance;
        return this;
    }

    public ClassMappingBuilder withFields(List<FieldMapping> fields) {
        this.fields = fields;
        return this;
    }

    public ClassMappingBuilder withJavaFieldGroupedByColumn(Map<String, NativeMapping> javaFieldGroupedByColumn) {
        this.javaFieldGroupedByColumn = javaFieldGroupedByColumn;
        return this;
    }

    public ClassMappingBuilder withFieldsGroupedByName(Map<String, FieldMapping> fieldsGroupedByName) {
        this.fieldsGroupedByName = fieldsGroupedByName;
        return this;
    }

    public ClassMappingBuilder withInstanceSupplier(InstanceSupplier instanceSupplier) {
        this.instanceSupplier = instanceSupplier;
        return this;
    }

    public ClassMapping build() {
        return new DefaultClassMapping(name, fieldsName, classInstance, fields,
                 javaFieldGroupedByColumn, fieldsGroupedByName, instanceSupplier);
    }
}