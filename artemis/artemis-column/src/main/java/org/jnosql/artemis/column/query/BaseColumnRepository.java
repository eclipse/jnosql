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
package org.jnosql.artemis.column.query;

import org.jnosql.aphrodite.antlr.method.DeleteMethodFactory;
import org.jnosql.aphrodite.antlr.method.SelectMethodFactory;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.DynamicReturn;
import org.jnosql.artemis.util.ParamsBinder;
import org.jnosql.diana.Params;
import org.jnosql.diana.Sort;
import org.jnosql.diana.column.ColumnDeleteQuery;
import org.jnosql.diana.column.ColumnObserverParser;
import org.jnosql.diana.column.ColumnQuery;
import org.jnosql.diana.column.query.ColumnDeleteQueryParams;
import org.jnosql.diana.column.query.ColumnQueryParams;
import org.jnosql.diana.column.query.DeleteQueryConverter;
import org.jnosql.diana.column.query.SelectQueryConverter;
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.SelectQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class BaseColumnRepository {

    protected abstract Converters getConverters();

    protected abstract ClassMapping getClassMapping();

    private ColumnObserverParser parser;

    private ParamsBinder paramsBinder;


    protected ColumnQuery getQuery(Method method, Object[] args) {
        SelectMethodFactory selectMethodFactory = SelectMethodFactory.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, getClassMapping().getName());
        SelectQueryConverter converter = SelectQueryConverter.get();
        ColumnQueryParams queryParams = converter.apply(selectQuery, getParser());
        ColumnQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return getQuerySorts(args, query);
    }

    protected ColumnDeleteQuery getDeleteQuery(Method method, Object[] args) {
        DeleteMethodFactory deleteMethodFactory = DeleteMethodFactory.get();
        DeleteQuery deleteQuery = deleteMethodFactory.apply(method, getClassMapping().getName());
        DeleteQueryConverter converter = DeleteQueryConverter.get();
        ColumnDeleteQueryParams queryParams = converter.apply(deleteQuery, getParser());
        ColumnDeleteQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return query;
    }

    protected ColumnQuery getQuerySorts(Object[] args, ColumnQuery query) {
        List<Sort> sorts = DynamicReturn.findSorts(args);
        if (!sorts.isEmpty()) {
            List<Sort> newOrders = new ArrayList<>();
            newOrders.addAll(query.getSorts());
            newOrders.addAll(sorts);
            return new ArtemisColumnQuery(newOrders, query.getLimit(), query.getSkip(),
                    query.getCondition().orElse(null), query.getColumnFamily());
        }
        return query;
    }


    protected ColumnObserverParser getParser() {
        if (parser == null) {
            this.parser = new RepositoryColumnObserverParser(getClassMapping());
        }
        return parser;
    }

    protected ParamsBinder getParamsBinder() {
        if (Objects.isNull(paramsBinder)) {
            this.paramsBinder = new ParamsBinder(getClassMapping(), getConverters());
        }
        return paramsBinder;
    }

}
