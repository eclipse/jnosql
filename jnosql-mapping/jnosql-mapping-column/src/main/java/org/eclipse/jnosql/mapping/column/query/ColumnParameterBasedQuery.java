/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.data.Sort;
import jakarta.data.page.Pageable;
import jakarta.enterprise.inject.spi.CDI;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.column.MappingColumnQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.eclipse.jnosql.mapping.core.util.ConverterUtil.getValue;

/**
 * The implementation of Parameter-based Conditions determine the implementation of the method to Column query
 */
public class ColumnParameterBasedQuery {

    private static final ColumnCondition[] CONDITIONS = new ColumnCondition[0];

    public ColumnQuery toQuery(Map<String, Object> params, Pageable pageable, EntityMetadata entityMetadata) {
        var convert = CDI.current().select(Converters.class).get();
        var conditions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            conditions.add(getCondition(convert, entityMetadata, entry));
        }

        var columnCondition = ColumnCondition.and(conditions.toArray(CONDITIONS));
        var sorts = pageable.sorts();
        long limit = pageable.size();
        long skip = NoSQLPage.skip(pageable);
        var columnFamily = entityMetadata.name();
        return new MappingColumnQuery(sorts, limit, skip, columnCondition, columnFamily);
    }

    private ColumnCondition getCondition(Converters convert, EntityMetadata entityMetadata, Map.Entry<String, Object> entry) {
        var name = entityMetadata.fieldMapping(entry.getKey())
                .map(FieldMetadata::name)
                .orElse(entry.getKey());
        var value = getValue(entry.getValue(), entityMetadata, entry.getKey(), convert);
        return ColumnCondition.eq(name, value);
    }
}
