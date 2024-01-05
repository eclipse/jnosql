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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.data.repository.By;
import org.eclipse.jnosql.communication.column.ColumnDeleteQuery;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * Template method to Repository proxy on column
 *
 * @param <T> the entity type
 * @param <K> the K entity
 */
public abstract class AbstractColumnRepositoryProxy<T, K> extends BaseColumnRepository<T, K>  {

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
    protected Object executeDeleteByAll(Object instance, Method method, Object[] params) {
        ColumnDeleteQuery deleteQuery = deleteQuery(method, params);
        template().delete(deleteQuery);
        return Void.class;
    }

    @Override
    protected Object executeFindAll(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        var query = ColumnQuery.select().from(entityMetadata().name()).build();
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
        var parameters = method.getParameters();
        Map<String, Object> paramsValue = new HashMap<>();
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            By annotation = parameter.getAnnotation(By.class);
            if(annotation != null) {
                paramsValue.put(annotation.value(), params[index]);
            }
        }
        var query = ColumnParameterBasedQuery.INSTANCE.toQuery(paramsValue, entityMetadata());
        return executeFindByQuery(method, params, type, updateQueryDynamically(params, query));
    }

}
