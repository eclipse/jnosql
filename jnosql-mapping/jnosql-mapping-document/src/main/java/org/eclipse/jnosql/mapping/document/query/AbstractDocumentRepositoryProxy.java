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

import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;

import java.lang.reflect.Method;

import static org.eclipse.jnosql.communication.document.DocumentQuery.select;

/**
 * Template method to Repository proxy on column
 *
 * @param <T> the entity type
 * @param <K> the key type
 */
public abstract class AbstractDocumentRepositoryProxy<T, K> extends BaseDocumentRepository<T, K> {


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
        DocumentDeleteQuery documentDeleteQuery = deleteQuery(method, params);
        template().delete(documentDeleteQuery);
        return null;
    }

    @Override
    protected Object executeFindAll(Object instance, Method method, Object[] params) {
        Class<?> typeClass = entityMetadata().type();
        DocumentQuery queryFindAll = select().from(entityMetadata().name()).build();
        return executeFindByQuery(method, params, typeClass, updateQueryDynamically(params, queryFindAll));
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


}
