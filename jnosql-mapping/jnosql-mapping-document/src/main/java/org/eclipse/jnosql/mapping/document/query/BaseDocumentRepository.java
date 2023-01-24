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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.data.repository.Page;
import jakarta.data.repository.Pageable;
import jakarta.data.repository.Sort;
import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.document.DeleteQueryParser;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentDeleteQueryParams;
import org.eclipse.jnosql.communication.document.DocumentObserverParser;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.communication.document.DocumentQueryParams;
import org.eclipse.jnosql.communication.document.SelectQueryParser;
import org.eclipse.jnosql.communication.query.DeleteQuery;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.method.DeleteMethodProvider;
import org.eclipse.jnosql.communication.query.method.SelectMethodProvider;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.NoSQLPage;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.document.MappingDocumentQuery;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.repository.SpecialParameters;
import org.eclipse.jnosql.mapping.util.ParamsBinder;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toUnmodifiableList;

public abstract class BaseDocumentRepository<T> {

    private static final SelectQueryParser SELECT_PARSER = new SelectQueryParser();

    private static final DeleteQueryParser DELETE_PARSER = new DeleteQueryParser();
    private static final Object[] EMPTY_PARAM = new Object[0];


    protected abstract Converters getConverters();

    protected abstract EntityMetadata getEntityMetadata();

    protected abstract JNoSQLDocumentTemplate getTemplate();

    private DocumentObserverParser parser;

    private ParamsBinder paramsBinder;


    protected DocumentQuery getQuery(Method method, Object[] args) {
        SelectMethodProvider provider = SelectMethodProvider.INSTANCE;
        SelectQuery selectQuery = provider.apply(method, getEntityMetadata().getName());
        DocumentQueryParams queryParams = SELECT_PARSER.apply(selectQuery, getParser());
        DocumentQuery query = queryParams.query();
        Params params = queryParams.params();
        getParamsBinder().bind(params, getArgs(args), method);
        return updateQueryDynamically(getArgs(args), query);
    }


    protected DocumentDeleteQuery getDeleteQuery(Method method, Object[] args) {
        DeleteMethodProvider deleteMethodFactory = DeleteMethodProvider.INSTANCE;
        DeleteQuery deleteQuery = deleteMethodFactory.apply(method, getEntityMetadata().getName());
        DocumentDeleteQueryParams queryParams = DELETE_PARSER.apply(deleteQuery, getParser());
        DocumentDeleteQuery query = queryParams.query();
        Params params = queryParams.params();
        getParamsBinder().bind(params, getArgs(args), method);
        return query;
    }

    private static Object[] getArgs(Object[] args) {
        return args == null ? EMPTY_PARAM : args;
    }


    protected DocumentQuery updateQueryDynamically(Object[] args, DocumentQuery query) {
        SpecialParameters special = DynamicReturn.findSpecialParameters(args);

        if (special.isEmpty()) {
            return query;
        }

        if (special.hasOnlySort()) {
            List<Sort> sorts = new ArrayList<>();
            sorts.addAll(query.sorts());
            sorts.addAll(special.sorts());
            return new MappingDocumentQuery(sorts, query.limit(),
                    query.skip(),
                    query.condition().orElse(null),
                    query.name());
        }

        return special.pageable().<DocumentQuery>map(p -> {
            long size = p.size();
            long skip = NoSQLPage.skip(p);
            List<Sort> sorts = query.sorts();
            if (!special.sorts().isEmpty()) {
                sorts = new ArrayList<>(query.sorts());
                sorts.addAll(special.sorts());
            }
            return new MappingDocumentQuery(sorts, size, skip,
                    query.condition().orElse(null), query.name());
        }).orElse(query);

    }

    protected DocumentObserverParser getParser() {
        if (parser == null) {
            this.parser = new RepositoryDocumentObserverParser(getEntityMetadata());
        }
        return parser;
    }

    protected ParamsBinder getParamsBinder() {
        if (Objects.isNull(paramsBinder)) {
            this.paramsBinder = new ParamsBinder(getEntityMetadata(), getConverters());
        }
        return paramsBinder;
    }

    protected Object executeQuery(Method method, Object[] args, Class<?> typeClass, DocumentQuery query) {
        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(() -> getTemplate().select(query))
                .withSingleResult(() -> getTemplate().singleResult(query))
                .withPagination(DynamicReturn.findPageable(args))
                .withStreamPagination(streamPagination(query))
                .withSingleResultPagination(getSingleResult(query))
                .withPage(getPage(query))
                .build();
        return dynamicReturn.execute();
    }

    protected Function<Pageable, Page<T>> getPage(DocumentQuery query) {
        return p -> {
            Stream<T> entities = getTemplate().select(query);
            return NoSQLPage.of(entities.collect(toUnmodifiableList()), p);
        };
    }

    protected Function<Pageable, Optional<T>> getSingleResult(DocumentQuery query) {
        return p -> getTemplate().singleResult(query);
    }

    protected Function<Pageable, Stream<T>> streamPagination(DocumentQuery query) {
        return p -> getTemplate().select(query);
    }

}
