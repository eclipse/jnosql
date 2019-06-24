/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
 *
 */
package org.jnosql.diana.column.query;

import jakarta.nosql.Params;
import jakarta.nosql.QueryException;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.query.JSONQueryValue;

import java.util.List;

/**
 * A template class to Update and Insert query parser to extract the condition
 */
abstract class ConditionQueryParser {


    protected ColumnEntity getEntity(ConditionQuerySupplier query, String columnFamily, Params params,
                                     ColumnObserverParser observer) {
        ColumnEntity entity = ColumnEntity.of(columnFamily);

        if (query.useJSONCondition()) {
            JSONQueryValue jsonValue = query.getValue()
                    .orElseThrow(() -> new QueryException("It is an invalid state of" +
                    " either Update or Insert."));
            List<Column> columns = JsonObjects.getColumns(jsonValue.get());
            entity.addAll(columns);
            return entity;
        }

        query.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily))
                .map(ColumnCondition::getColumn)
                .forEach(entity::add);
        return entity;
    }
}
