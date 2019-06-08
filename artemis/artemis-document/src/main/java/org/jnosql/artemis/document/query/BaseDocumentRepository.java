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
package org.jnosql.artemis.document.query;

import org.jnosql.aphrodite.antlr.method.DeleteMethodFactory;
import org.jnosql.aphrodite.antlr.method.SelectMethodFactory;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.DynamicReturn;
import org.jnosql.artemis.util.ParamsBinder;
import org.jnosql.diana.api.Params;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.document.query.DeleteQueryConverter;
import org.jnosql.diana.api.document.query.DocumentDeleteQueryParams;
import org.jnosql.diana.api.document.query.DocumentQueryParams;
import org.jnosql.diana.api.document.query.SelectQueryConverter;
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.SelectQuery;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class BaseDocumentRepository {


    protected abstract Converters getConverters();

    protected abstract ClassMapping getClassMapping();

    private DocumentObserverParser parser;

    private ParamsBinder paramsBinder;


    protected DocumentQuery getQuery(Method method, Object[] args) {
        SelectMethodFactory selectMethodFactory = SelectMethodFactory.get();
        SelectQuery selectQuery = selectMethodFactory.apply(method, getClassMapping().getName());
        SelectQueryConverter converter = SelectQueryConverter.get();
        DocumentQueryParams queryParams = converter.apply(selectQuery, getParser());
        DocumentQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return getQuerySorts(args, query);
    }

    protected DocumentQuery getQuerySorts(Object[] args, DocumentQuery query) {
        List<Sort> sorts = DynamicReturn.findSorts(args);
        if (!sorts.isEmpty()) {
            List<Sort> newOrders = new ArrayList<>();
            newOrders.addAll(query.getSorts());
            newOrders.addAll(sorts);
            return new ArtemisDocumentQuery(newOrders, query.getLimit(), query.getSkip(),
                    query.getCondition().orElse(null), query.getDocumentCollection());
        }
        return query;
    }

    protected DocumentDeleteQuery getDeleteQuery(Method method, Object[] args) {
        DeleteMethodFactory deleteMethodFactory = DeleteMethodFactory.get();
        DeleteQuery deleteQuery = deleteMethodFactory.apply(method, getClassMapping().getName());
        DeleteQueryConverter converter = DeleteQueryConverter.get();
        DocumentDeleteQueryParams queryParams = converter.apply(deleteQuery, getParser());
        DocumentDeleteQuery query = queryParams.getQuery();
        Params params = queryParams.getParams();
        getParamsBinder().bind(params, args, method);
        return query;
    }


    protected DocumentObserverParser getParser() {
        if (parser == null) {
            this.parser = new RepositoryDocumentObserverParser(getClassMapping());
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
