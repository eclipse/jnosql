import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParser;
import jakarta.nosql.column.DeleteQueryConverter;
import jakarta.nosql.column.SelectQueryConverter;

/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
module org.eclipse.jnosql.communication.column {
    requires jakarta.nosql.communication.column;
    requires jakarta.nosql.communication.core;
    requires org.eclipse.jnosql.communication.core;
    requires jakarta.nosql.communication.query;
    requires java.json.bind;
    requires java.json;

    opens org.eclipse.jnosql.communication.column.query;

    provides Column.ColumnProvider with org.eclipse.jnosql.communication.column.DefaultColumnProvider;
    provides ColumnCondition.ColumnConditionProvider with org.eclipse.jnosql.communication.column.DefaultColumnConditionProvider;
    provides ColumnDeleteQuery.ColumnDeleteProvider with org.eclipse.jnosql.communication.column.query.DefaultColumnDeleteProvider;
    provides ColumnDeleteQuery.ColumnDeleteQueryBuilderProvider with org.eclipse.jnosql.communication.column.query.DefaultColumnDeleteQueryBuilderProvider;
    provides ColumnEntity.ColumnEntityProvider with org.eclipse.jnosql.communication.column.DefaultColumnEntityProvider;
    provides ColumnQuery.ColumnQueryBuilderProvider with org.eclipse.jnosql.communication.column.query.DefaultColumnQueryBuilderProvider;
    provides ColumnQuery.ColumnSelectProvider with org.eclipse.jnosql.communication.column.query.DefaultColumnSelectProvider;
    provides ColumnQueryParser with org.eclipse.jnosql.communication.column.query.DefaultColumnQueryParser;
    provides DeleteQueryConverter with org.eclipse.jnosql.communication.column.query.DeleteQueryParser;
    provides SelectQueryConverter with org.eclipse.jnosql.communication.column.query.SelectQueryParser;
}