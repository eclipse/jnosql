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
package org.eclipse.jnosql.mapping.semistructured.query;

import jakarta.data.Sort;
import jakarta.data.repository.Find;
import jakarta.data.repository.OrderBy;
import jakarta.data.repository.Query;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.core.repository.RepositoryReflectionUtils;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Template method to Repository proxy on column
 *
 * @param <T> the entity type
 * @param <K> the K entity
 */
public abstract class AbstractSemiStructuredRepositoryProxy<T, K> extends BaseSemiStructuredRepository<T, K> {

    @Override
    protected Object executeQuery(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        var entity = entityMetadata().name();
        var methodReturn = DynamicQueryMethodReturn.builder()
                .withArgs(params)
                .withMethod(method)
                .withTypeClass(type)
                .withPrepareConverter(textQuery -> {
                    var prepare = (org.eclipse.jnosql.mapping.semistructured.PreparedStatement)template().prepare(textQuery, entity);
                    prepare.setSelectMapper(query -> updateQueryDynamically(params, query));
                    return prepare;
                })
                .build();
        return methodReturn.execute();
    }

    @Override
    protected Object executeCursorPagination(Object instance, Method method, Object[] params) {

        if (method.getAnnotation(Query.class) != null) {
            var entity = entityMetadata().name();
            var textQuery = method.getAnnotation(Query.class).value();
            var prepare = (org.eclipse.jnosql.mapping.semistructured.PreparedStatement)template().prepare(textQuery, entity);
            var argsParams = RepositoryReflectionUtils.INSTANCE.getParams(method, params);
            argsParams.forEach(prepare::bind);
            var selectQuery = updateQueryDynamically(params, prepare.selectQuery().orElseThrow());
            var special = DynamicReturn.findSpecialParameters(params, sortParser());
            var pageRequest = special.pageRequest()
                    .orElseThrow(() -> new IllegalArgumentException("Pageable is required in the method signature as parameter at " + method));

            return this.template().selectCursor(selectQuery, pageRequest);
        } else if (method.getAnnotation(Find.class) == null) {
            var query = query(method, params);
            var special = DynamicReturn.findSpecialParameters(params, sortParser());
            var pageRequest = special.pageRequest()
                    .orElseThrow(() -> new IllegalArgumentException("Pageable is required in the method signature as parameter at " + method));
            return this.template().selectCursor(query, pageRequest);
        } else {
            var parameters = RepositoryReflectionUtils.INSTANCE.getBy(method, params);
            var query = SemiStructuredParameterBasedQuery.INSTANCE.toQuery(parameters, getSorts(method, entityMetadata()), entityMetadata());
            var special = DynamicReturn.findSpecialParameters(params, sortParser());
            var pageRequest = special.pageRequest()
                    .orElseThrow(() -> new IllegalArgumentException("Pageable is required in the method signature as parameter at " + method));
            return this.template().selectCursor(query, pageRequest);
        }
    }


    @Override
    protected Object executeDeleteByAll(Object instance, Method method, Object[] params) {
        DeleteQuery deleteQuery = deleteQuery(method, params);
        template().delete(deleteQuery);
        return Void.class;
    }

    @Override
    protected Object executeFindAll(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        var query = org.eclipse.jnosql.communication.semistructured.SelectQuery.select().from(entityMetadata().name()).build();
        return executeFindByQuery(method, params, type, updateQueryDynamically(params, query));
    }

    @Override
    protected Object executeExistByQuery(Object instance, Method method, Object[] params) {
        return executeExistsByQuery(query(method, params));
    }

    @Override
    protected Object executeCountByQuery(Object instance, Method method, Object[] params) {
        return executeCountByQuery(query(method, params));
    }

    @Override
    protected Object executeFindByQuery(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        return executeFindByQuery(method, params, type, query(method, params));
    }

    @Override
    protected Object executeParameterBased(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        Map<String, Object> parameters = RepositoryReflectionUtils.INSTANCE.getBy(method, params);
        var query = SemiStructuredParameterBasedQuery.INSTANCE.toQuery(parameters, getSorts(method, entityMetadata()), entityMetadata());
        return executeFindByQuery(method, params, type, updateQueryDynamically(params, query));
    }

    private static List<Sort<?>> getSorts(Method method, EntityMetadata metadata) {
        List<Sort<?>> sorts = new ArrayList<>();
        OrderBy[] orders = method.getAnnotationsByType(OrderBy.class);
        Stream.of(orders)
                .map(o -> (o.descending() ? Sort.desc(metadata.columnField(o.value())) : Sort.asc(metadata.columnField(o.value()))))
                .forEach(sorts::add);
        return sorts;
    }

}
