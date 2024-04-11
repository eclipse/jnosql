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
import jakarta.data.page.PageRequest;
import jakarta.data.repository.Find;
import jakarta.data.repository.OrderBy;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.core.repository.RepositoryReflectionUtils;
import org.eclipse.jnosql.mapping.core.repository.SpecialParameters;

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
        DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                .withArgs(params)
                .withMethod(method)
                .withTypeClass(type)
                .withPrepareConverter(q -> template().prepare(q))
                .withQueryConverter(q -> template().query(q)).build();
        return methodReturn.execute();
    }

    @Override
    protected Object executeCursorPagination(Object instance, Method method, Object[] params) {
        if (method.getAnnotation(Find.class) == null) {
            var query = query(method, params);
            SpecialParameters special = DynamicReturn.findSpecialParameters(params);
            PageRequest pageRequest = special.pageRequest()
                    .orElseThrow(() -> new IllegalArgumentException("Pageable is required in the method signature as parameter at " + method));
            return this.template().selectCursor(query, pageRequest);
        } else {
            Map<String, Object> parameters = RepositoryReflectionUtils.INSTANCE.getBy(method, params);
            var query = SemiStructuredParameterBasedQuery.INSTANCE.toQuery(parameters, getSorts(method), entityMetadata());
            SpecialParameters special = DynamicReturn.findSpecialParameters(params);
            PageRequest pageRequest = special.pageRequest()
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
        var query = SemiStructuredParameterBasedQuery.INSTANCE.toQuery(parameters, getSorts(method), entityMetadata());
        return executeFindByQuery(method, params, type, updateQueryDynamically(params, query));
    }

    private static List<Sort<?>> getSorts(Method method) {
        List<Sort<?>> sorts = new ArrayList<>();
        OrderBy[] orders = method.getAnnotationsByType(OrderBy.class);
        Stream.of(orders)
                .map(o -> (o.descending() ? Sort.desc(o.value()) : Sort.asc(o.value())))
                .forEach(sorts::add);
        return sorts;
    }

}
