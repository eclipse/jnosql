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
package org.eclipse.jnosql.mapping.column;


import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import org.eclipse.jnosql.mapping.reflection.FieldValue;

import java.util.List;

/**
 * The specialist {@link FieldValue} to column
 */
public interface ColumnFieldValue extends FieldValue {


    /**
     * Converts an entity to a {@link List} of columns
     * @param converter the converter
     * @param converters the converters
     * @param <X> the type of the entity attribute
     * @param <Y> the type of the database column
     * @return a {@link List} of columns from the field
     */
    <X, Y> List<Column> toColumn(ColumnEntityConverter converter, Converters converters);

}