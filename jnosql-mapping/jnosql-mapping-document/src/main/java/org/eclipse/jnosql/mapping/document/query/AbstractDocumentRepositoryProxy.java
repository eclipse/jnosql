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

import jakarta.data.exceptions.MappingException;
import jakarta.enterprise.inject.spi.CDI;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.core.query.AnnotationOperation;
import org.eclipse.jnosql.mapping.core.query.RepositoryType;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.core.repository.ThrowingSupplier;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.eclipse.jnosql.communication.document.DocumentQuery.select;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.DELETE;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.INSERT;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.SAVE;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.UPDATE;

/**
 * Template method to Repository proxy on column
 *
 * @param <T> the entity type
 * @param <K> the key type
 */
public abstract class AbstractDocumentRepositoryProxy<T, K> extends BaseDocumentRepository<T> implements InvocationHandler {

    protected abstract AbstractRepository<T, K> repository();

    protected abstract Class<?> repositoryType();

    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method, repositoryType());
        Class<?> typeClass = entityMetadata().type();

        switch (type) {
            case DEFAULT -> {
                return unwrapInvocationTargetException(() -> method.invoke(repository(), args));
            }
            case FIND_BY -> {
                return executeFindByQuery(method, args, typeClass, query(method, args));
            }
            case COUNT_BY -> {
                return executeCountByQuery(query(method, args));
            }
            case EXISTS_BY -> {
                return executeExistsByQuery(query(method, args));
            }
            case FIND_ALL -> {
                DocumentQuery queryFindAll = select().from(entityMetadata().name()).build();
                return executeFindByQuery(method, args, typeClass, updateQueryDynamically(args, queryFindAll));
            }
            case DELETE_BY -> {
                DocumentDeleteQuery documentDeleteQuery = deleteQuery(method, args);
                template().delete(documentDeleteQuery);
                return null;
            }
            case OBJECT_METHOD -> {
                return unwrapInvocationTargetException(() -> method.invoke(this, args));
            }
            case DEFAULT_METHOD -> {
                return unwrapInvocationTargetException(() -> InvocationHandler.invokeDefault(instance, method, args));
            }
            case ORDER_BY ->
                    throw new MappingException("Eclipse JNoSQL has not support for method that has OrderBy annotation");
            case QUERY -> {
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> template().prepare(q))
                        .withQueryConverter(q -> template().query(q)).build();
                return methodReturn.execute();
            }
            case CUSTOM_REPOSITORY -> {
                Object customRepository = CDI.current().select(method.getDeclaringClass()).get();
                return unwrapInvocationTargetException(() -> method.invoke(customRepository, args));
            }
            case SAVE -> {
                return unwrapInvocationTargetException(() -> SAVE.invoke(new AnnotationOperation.Operation(method, args, repository())));
            }
            case INSERT -> {
                return unwrapInvocationTargetException(() -> INSERT.invoke(new AnnotationOperation.Operation(method, args, repository())));
            }
            case DELETE -> {
                return unwrapInvocationTargetException(() -> DELETE.invoke(new AnnotationOperation.Operation(method, args, repository())));
            }
            case UPDATE -> {
                return unwrapInvocationTargetException(() -> UPDATE.invoke(new AnnotationOperation.Operation(method, args, repository())));
            }
            default -> {
                return Void.class;
            }
        }
    }

    private Object unwrapInvocationTargetException(ThrowingSupplier<Object> supplier) throws Throwable {
        try {
            return supplier.get();
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
