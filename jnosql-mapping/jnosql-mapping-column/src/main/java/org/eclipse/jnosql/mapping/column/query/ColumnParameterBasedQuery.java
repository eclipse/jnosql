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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.IntFunction;

import static org.eclipse.jnosql.mapping.core.util.ConverterUtil.getValue;

/**
 * The ColumnParameterBasedQuery class is responsible for generating Column queries based on a set of parameters.
 * It leverages the provided parameters, pageable information, and entity metadata to construct a ColumnQuery object
 * tailored for querying a specific entity's columns.
 */
public enum ColumnParameterBasedQuery {


    INSTANCE;
    private static final IntFunction<ColumnCondition[]> TO_ARRAY = ColumnCondition[]::new;

    /**
     * Constructs a ColumnQuery based on the provided parameters, pageable information, and entity metadata.
     *
     * @param params          The map of parameters used for filtering columns.
     * @param pageable        The Pageable object containing sorting and pagination information.
     * @param entityMetadata  Metadata describing the structure of the entity.
     * @return                 A ColumnQuery instance tailored for the specified entity.
     */
    public ColumnQuery toQuery(Map<String, Object> params, Pageable pageable, EntityMetadata entityMetadata) {
        var convert = CDI.current().select(Converters.class).get();
        List<ColumnCondition> conditions = new ArrayList<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            conditions.add(getCondition(convert, entityMetadata, entry));
        }

        var columnCondition = columnCondition(conditions);
        var optional = Optional.ofNullable(pageable);
        var sorts = optional.map(Pageable::sorts).orElse(Collections.emptyList());
        long limit = optional.map(Pageable::size).orElse(0);
        long skip = optional.map(p -> NoSQLPage.skip(pageable)).orElse(0L);
        var columnFamily = entityMetadata.name();
        return new MappingColumnQuery(sorts, limit, skip, columnCondition, columnFamily);
    }

    private ColumnCondition columnCondition(List<ColumnCondition> conditions) {
        if(conditions.isEmpty()){
            return null;
        }
        else if(conditions.size() == 1){
            return conditions.get(0);
        }
        return ColumnCondition.and(conditions.toArray(TO_ARRAY));
    }

    private ColumnCondition getCondition(Converters convert, EntityMetadata entityMetadata, Map.Entry<String, Object> entry) {
        var name = entityMetadata.fieldMapping(entry.getKey())
                .map(FieldMetadata::name)
                .orElse(entry.getKey());
        var value = getValue(entry.getValue(), entityMetadata, entry.getKey(), convert);
        return ColumnCondition.eq(name, value);
    }
}
