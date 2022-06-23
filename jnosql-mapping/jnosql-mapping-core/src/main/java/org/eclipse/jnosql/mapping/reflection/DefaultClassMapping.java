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


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

class DefaultClassMapping implements ClassMapping {


    private final String name;

    private final List<String> fieldsName;

    private final Class<?> classInstance;

    private final List<FieldMapping> fields;

    private final InstanceSupplier instanceSupplier;

    private final Map<String, NativeMapping> javaFieldGroupedByColumn;

    private final Map<String, FieldMapping> fieldsGroupedByName;

    private final FieldMapping id;

    DefaultClassMapping(String name, List<String> fieldsName, Class<?> classInstance,
                        List<FieldMapping> fields,
                        Map<String, NativeMapping> javaFieldGroupedByColumn,
                        Map<String, FieldMapping> fieldsGroupedByName, InstanceSupplier instanceSupplier) {
        this.name = name;
        this.fieldsName = fieldsName;
        this.classInstance = classInstance;
        this.fields = fields;
        this.fieldsGroupedByName = fieldsGroupedByName;
        this.javaFieldGroupedByColumn = javaFieldGroupedByColumn;
        this.instanceSupplier = instanceSupplier;
        this.id = fields.stream().filter(FieldMapping::isId).findFirst().orElse(null);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getFieldsName() {
        return fieldsName;
    }

    @Override
    public Class<?> getClassInstance() {
        return classInstance;
    }

    @Override
    public List<FieldMapping> getFields() {
        return fields;
    }

    @Override
    public <T> T newInstance() {
        return (T) instanceSupplier.get();
    }

    @Override
    public String getColumnField(String javaField) {
        requireNonNull(javaField, "javaField is required");
        return ofNullable(javaFieldGroupedByColumn.get(javaField))
                .map(NativeMapping::getNativeField).orElse(javaField);

    }

    @Override
    public Optional<FieldMapping> getFieldMapping(String javaField) {
        requireNonNull(javaField, "javaField is required");
        return ofNullable(javaFieldGroupedByColumn.get(javaField))
                .map(NativeMapping::getFieldMapping);
    }

    @Override
    public Map<String, FieldMapping> getFieldsGroupByName() {
        return fieldsGroupedByName;
    }

    @Override
    public Optional<FieldMapping> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultClassMapping)) {
            return false;
        }
        DefaultClassMapping that = (DefaultClassMapping) o;
        return Objects.equals(classInstance, that.classInstance);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(classInstance);
    }

    @Override
    public String toString() {
        return  "DefaultClassMapping{" + "name='" + name + '\'' +
                ", fieldsName=" + fieldsName +
                ", classInstance=" + classInstance +
                ", fields=" + fields +
                ", javaFieldGroupedByColumn=" + javaFieldGroupedByColumn +
                ", fieldsGroupedByName=" + fieldsGroupedByName +
                ", id=" + id +
                '}';
    }

    /**
     * Creates a builder
     *
     * @return {@link ClassMappingBuilder}
     */
    static ClassMappingBuilder builder() {
        return new ClassMappingBuilder();
    }


}
