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
package org.jnosql.artemis.column;

import org.jnosql.artemis.AttributeConverter;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.reflection.FieldMapping;
import org.jnosql.artemis.reflection.FieldType;
import org.jnosql.artemis.reflection.FieldValue;
import org.jnosql.artemis.reflection.GenericFieldMapping;
import org.jnosql.diana.column.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.jnosql.artemis.reflection.FieldType.COLLECTION;
import static org.jnosql.artemis.reflection.FieldType.EMBEDDED;
import static org.jnosql.artemis.reflection.FieldType.SUBENTITY;

final class DefaultColumnFieldValue implements ColumnFieldValue {

    private final FieldValue fieldValue;

    private DefaultColumnFieldValue(FieldValue fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public Object getValue() {
        return fieldValue.getValue();
    }

    @Override
    public FieldMapping getField() {
        return fieldValue.getField();
    }

    @Override
    public boolean isNotEmpty() {
        return fieldValue.isNotEmpty();
    }

    public List<Column> toColumn(ColumnEntityConverter converter, Converters converters) {

        if (EMBEDDED.equals(getType())) {
            return converter.toColumn(getValue()).getColumns();
        } else if (SUBENTITY.equals(getType())) {
            return singletonList(Column.of(getName(), converter.toColumn(getValue()).getColumns()));
        } else if (isEmbeddableCollection()) {
            return singletonList(Column.of(getName(), getColumns(converter)));
        }
        Optional<Class<? extends AttributeConverter>> optionalConverter = getField().getConverter();
        if (optionalConverter.isPresent()) {
            AttributeConverter attributeConverter = converters.get(optionalConverter.get());
            return singletonList(Column.of(getName(), attributeConverter.convertToDatabaseColumn(getValue())));
        }

        return singletonList(Column.of(getName(), getValue()));
    }

    private List<List<Column>> getColumns(ColumnEntityConverter converter) {
        List<List<Column>> columns = new ArrayList<>();
        for (Object element : (Iterable) getValue()) {
            columns.add(converter.toColumn(element).getColumns());
        }
        return columns;
    }

    private boolean isEmbeddableCollection() {
        return COLLECTION.equals(getType()) && isEmbeddableElement();
    }

    private FieldType getType() {
        return getField().getType();
    }

    private String getName() {
        return getField().getName();
    }

    private boolean isEmbeddableElement() {
        return ((GenericFieldMapping) getField()).isEmbeddable();
    }

    @Override
    public String toString() {
        return  "ColumnFieldValue{" + "fieldValue=" + fieldValue +
                '}';
    }

    static ColumnFieldValue of(Object value, FieldMapping field) {
        return new DefaultColumnFieldValue(FieldValue.of(value, field));
    }
}
