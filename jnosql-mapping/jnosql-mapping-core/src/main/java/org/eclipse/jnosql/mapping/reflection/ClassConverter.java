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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.jnosql.mapping.Convert;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@ApplicationScoped
class ClassConverter {

    private static final Logger LOGGER = Logger.getLogger(ClassConverter.class.getName());

    private Reflections reflections;
   private InstanceSupplierFactory instanceSupplierFactory;

    private ConstructorMetadataBuilder constructorMetadataBuilder;


    @Inject
    ClassConverter(Reflections reflections) {

        this.reflections = reflections;
        this.instanceSupplierFactory = new ReflectionInstanceSupplierFactory(reflections);
        this.constructorMetadataBuilder = new ConstructorMetadataBuilder(reflections);
    }

    ClassConverter() {
    }

    public EntityMetadata create(Class<?> entity) {

        long start = System.currentTimeMillis();
        String entityName = reflections.getEntityName(entity);

        List<FieldMapping> fields = reflections.getFields(entity)
                .stream().map(this::to).collect(toList());

        List<String> fieldsName = fields.stream().map(FieldMapping::name).collect(toList());

        Map<String, NativeMapping> nativeFieldGroupByJavaField =
                getNativeFieldGroupByJavaField(fields, "", "");

        Map<String, FieldMapping> fieldsGroupedByName = fields.stream()
                .collect(collectingAndThen(toMap(FieldMapping::name,
                        Function.identity()), Collections::unmodifiableMap));

        InstanceSupplier instanceSupplier = instanceSupplierFactory.apply(reflections.getConstructor(entity));
        InheritanceMetadata inheritance = reflections.getInheritance(entity).orElse(null);
        boolean hasInheritanceAnnotation = reflections.hasInheritanceAnnotation(entity);

        EntityMetadata mapping = DefaultEntityMetadata.builder().name(entityName)
                .type(entity)
                .fields(fields)
                .fieldsName(fieldsName)
                .instanceSupplier(instanceSupplier)
                .javaFieldGroupedByColumn(nativeFieldGroupByJavaField)
                .fieldsGroupedByName(fieldsGroupedByName)
                .inheritance(inheritance)
                .hasInheritanceAnnotation(hasInheritanceAnnotation)
                .constructor(constructorMetadataBuilder.build(entity))
                .build();

        long end = System.currentTimeMillis() - start;
        LOGGER.finest(String.format("Scanned the entity %s loaded with time of %d ms", entity.getName(), end));
        return mapping;
    }

    private Map<String, NativeMapping> getNativeFieldGroupByJavaField(List<FieldMapping> fields,
                                                                      String javaField, String nativeField) {

        Map<String, NativeMapping> nativeFieldGroupByJavaField = new HashMap<>();

        for (FieldMapping field : fields) {
            appendValue(nativeFieldGroupByJavaField, field, javaField, nativeField);
        }

        return nativeFieldGroupByJavaField;
    }

    private void appendValue(Map<String, NativeMapping> nativeFieldGroupByJavaField, FieldMapping field,
                             String javaField, String nativeField) {


        switch (field.type()) {
            case ENTITY -> appendFields(nativeFieldGroupByJavaField, field, javaField,
                    appendPreparePrefix(nativeField, field.name()));
            case EMBEDDED -> appendFields(nativeFieldGroupByJavaField, field, javaField, nativeField);
            case COLLECTION -> {
                if (((GenericFieldMapping) field).isEmbeddable()) {
                    Class<?> type = ((GenericFieldMapping) field).getElementType();
                    String nativeFieldAppended = appendPreparePrefix(nativeField, field.name());
                    appendFields(nativeFieldGroupByJavaField, field, javaField, nativeFieldAppended, type);
                    return;
                }
                appendDefaultField(nativeFieldGroupByJavaField, field, javaField, nativeField);
            }
            default -> appendDefaultField(nativeFieldGroupByJavaField, field, javaField, nativeField);
        }

    }

    private void appendDefaultField(Map<String, NativeMapping> nativeFieldGroupByJavaField,
                                    FieldMapping field, String javaField, String nativeField) {

        nativeFieldGroupByJavaField.put(javaField.concat(field.fieldName()),
                NativeMapping.of(nativeField.concat(field.name()), field));
    }

    private void appendFields(Map<String, NativeMapping> nativeFieldGroupByJavaField,
                              FieldMapping field,
                              String javaField, String nativeField) {

        Class<?> type = field.nativeField().getType();
        appendFields(nativeFieldGroupByJavaField, field, javaField, nativeField, type);
    }

    private void appendFields(Map<String, NativeMapping> nativeFieldGroupByJavaField,
                              FieldMapping field, String javaField, String nativeField,
                              Class<?> type) {

        Map<String, NativeMapping> entityMap = getNativeFieldGroupByJavaField(
                reflections.getFields(type)
                        .stream().map(this::to).collect(toList()),
                appendPreparePrefix(javaField, field.fieldName()), nativeField);

        String nativeElement = entityMap.values().stream().map(NativeMapping::getNativeField)
                .collect(Collectors.joining(","));

        nativeFieldGroupByJavaField.put(appendPrefix(javaField, field.fieldName()), NativeMapping.of(nativeElement, field));
        nativeFieldGroupByJavaField.putAll(entityMap);
    }

    private String appendPreparePrefix(String prefix, String field) {
        return appendPrefix(prefix, field).concat(".");
    }

    private String appendPrefix(String prefix, String field) {
        if (prefix.isEmpty()) {
            return field;
        } else {
            return prefix.concat(field);
        }
    }


    private FieldMapping to(Field field) {
        MappingType mappingType = MappingType.of(field);
        reflections.makeAccessible(field);
        Convert convert = field.getAnnotation(Convert.class);
        boolean id = reflections.isIdField(field);
        String columnName = id ? reflections.getIdName(field) : reflections.getColumnName(field);

        FieldMappingBuilder builder = new FieldMappingBuilder().withName(columnName)
                .withField(field).withType(mappingType).withId(id)
                .withReader(bean -> reflections.getValue(bean, field))
                .withWriter( (bean, value) -> reflections.setValue(bean, field, value));

        if (nonNull(convert)) {
            builder.withConverter(convert.value());
        }
        switch (mappingType) {
            case COLLECTION, MAP -> {
                builder.withTypeSupplier(field::getGenericType);
                return builder.buildGeneric();
            }
            case EMBEDDED -> {
                return builder.withEntityName(reflections.getEntityName(field.getType())).buildEmbedded();
            }
            default -> {
                return builder.buildDefault();
            }
        }
    }

}
