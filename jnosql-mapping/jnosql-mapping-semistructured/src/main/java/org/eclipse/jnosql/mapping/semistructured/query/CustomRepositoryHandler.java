/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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


import jakarta.data.page.CursoredPage;
import jakarta.data.page.Page;
import jakarta.data.repository.Query;
import jakarta.enterprise.inject.spi.CDI;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.core.query.AnnotationOperation;
import org.eclipse.jnosql.mapping.core.query.RepositoryType;
import org.eclipse.jnosql.mapping.core.repository.RepositoryReflectionUtils;
import org.eclipse.jnosql.mapping.core.repository.ThrowingSupplier;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.semistructured.SemiStructuredTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.DELETE;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.INSERT;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.SAVE;
import static org.eclipse.jnosql.mapping.core.query.AnnotationOperation.UPDATE;

/**
 * This class is the engine of a custom repository from  Jakarta Data specification.
 * The implementation is based on {@link InvocationHandler} and it's used to create a custom repository.
 */
public class CustomRepositoryHandler implements InvocationHandler {

    private static final Logger LOGGER = Logger.getLogger(CustomRepositoryHandler.class.getName());

    private static final Predicate<Class<?>> IS_ITERABLE = Iterable.class::isAssignableFrom;
    private static final Predicate<Class<?>> IS_STREAM = Stream.class::isAssignableFrom;
    private static final Predicate<Class<?>> IS_OPTIONAL = Optional.class::isAssignableFrom;
    private static final Predicate<Class<?>> IS_PAGE = Page.class::isAssignableFrom;
    private static final Predicate<Class<?>> IS_CURSOR_PAGE = CursoredPage.class::isAssignableFrom;
    private static final Predicate<Class<?>> IS_GENERIC_SUPPORTED_TYPE = IS_ITERABLE.or(IS_STREAM).or(IS_OPTIONAL).or(IS_PAGE).or(IS_CURSOR_PAGE);

    private final EntitiesMetadata entitiesMetadata;

    private final SemiStructuredTemplate template;

    private final Class<?> customRepositoryType;

    private final Converters converters;

    CustomRepositoryHandler(EntitiesMetadata entitiesMetadata, SemiStructuredTemplate template,
                            Class<?> customRepositoryType,
                            Converters converters) {
        this.entitiesMetadata = entitiesMetadata;
        this.template = template;
        this.customRepositoryType = customRepositoryType;
        this.converters = converters;
    }

    @Override
    public Object invoke(Object instance, Method method, Object[] params) throws Throwable {

        RepositoryType type = RepositoryType.of(method, customRepositoryType);
        LOGGER.info("Executing the method " + method + " with the parameters " + Arrays.toString(params) + " and the type " + type);

        switch (type) {
            case SAVE -> {
                return unwrapInvocationTargetException(() -> SAVE.invoke(new AnnotationOperation.Operation(method, params, repository(params, method))));
            }
            case INSERT -> {
                return unwrapInvocationTargetException(() -> INSERT.invoke(new AnnotationOperation.Operation(method, params, repository(params, method))));
            }
            case DELETE -> {
                return unwrapInvocationTargetException(() -> DELETE.invoke(new AnnotationOperation.Operation(method, params, repository(params, method))));
            }
            case UPDATE -> {
                return unwrapInvocationTargetException(() -> UPDATE.invoke(new AnnotationOperation.Operation(method, params, repository(params, method))));
            }
            case DEFAULT_METHOD -> {
                return unwrapInvocationTargetException(() -> InvocationHandler.invokeDefault(instance, method, params));
            }
            case OBJECT_METHOD -> {
                return unwrapInvocationTargetException(() -> unwrapInvocationTargetException(() -> method.invoke(this, params)));
            }
            case PARAMETER_BASED -> {
                return unwrapInvocationTargetException(() -> repository(method).executeParameterBased(instance, method, params));
            }
            case CURSOR_PAGINATION -> {
                return unwrapInvocationTargetException(() -> repository(method).executeCursorPagination(instance, method, params));
            }
            case FIND_ALL -> {
                return unwrapInvocationTargetException(() -> repository(method).executeFindAll(instance, method, params));
            }
            case FIND_BY -> {
                return unwrapInvocationTargetException(() -> repository(method).executeFindByQuery(instance, method, params));
            }
            case CUSTOM_REPOSITORY -> {
                Object customRepository = CDI.current().select(method.getDeclaringClass()).get();
                return unwrapInvocationTargetException(() -> method.invoke(customRepository, params));
            }
            case QUERY -> {
                var repositoryMetadata = repositoryMetadata(method);
                if (repositoryMetadata.metadata().isEmpty()) {
                    var query = method.getAnnotation(Query.class);
                    Map<String, Object> parameters = RepositoryReflectionUtils.INSTANCE.getParams(method, params);
                    var prepare = template.prepare(query.value());
                    parameters.forEach(prepare::bind);
                    if (prepare.isCount()) {
                        return prepare.count();
                    }
                    Stream<?> entities = prepare.result();
                    if (method.getReturnType().equals(long.class) || method.getReturnType().equals(Long.class)) {
                        return entities.count();
                    }
                    return Void.class;
                }
                return unwrapInvocationTargetException(() -> repository(method).invoke(instance, method, params));

            }
            case DELETE_BY, COUNT_ALL, COUNT_BY, EXISTS_BY ->
                    throw new UnsupportedOperationException("The custom repository does not support the method " + method);
            default -> {
                return Void.class;
            }
        }
    }

    private SemiStructuredRepositoryProxy<?, ?> repository(Method method) {
        RepositoryMetadata result = repositoryMetadata(method);
        Class<?> entityType = result.typeClass();
        return result.metadata().map(entityMetadata -> new SemiStructuredRepositoryProxy<>(template, entityMetadata, entityType, converters))
                .orElseThrow(() -> new UnsupportedOperationException("The repository does not support the method " + method));
    }

    private RepositoryMetadata repositoryMetadata(Method method) {
        Class<?> typeClass = method.getReturnType();
        if (typeClass.isArray()) {
            typeClass = typeClass.getComponentType();
        } else if (Iterable.class.isAssignableFrom(typeClass) || Stream.class.isAssignableFrom(typeClass) || Optional.class.isAssignableFrom(typeClass)) {
            typeClass = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        }
        Optional<EntityMetadata> metadata = entitiesMetadata.findByClassName(typeClass.getName());
        return new RepositoryMetadata(typeClass, metadata);
    }

    private record RepositoryMetadata(Class<?> typeClass, Optional<EntityMetadata> metadata) {
    }

    private AbstractRepository<?, ?> repository(Object[] params, Method method) {
        Class<?> typeClass = params[0].getClass();
        if (typeClass.isArray()) {
            typeClass = typeClass.getComponentType();
        } else if (IS_GENERIC_SUPPORTED_TYPE.test(typeClass)) {
            var entity = ((Iterable<?>) params[0]).iterator().next();
            typeClass = entity.getClass();
        }
        Optional<EntityMetadata> entity = entitiesMetadata.findByClassName(typeClass.getName());
        return entity.map(entityMetadata -> new SemiStructuredRepositoryProxy.SemiStructuredRepository<>(template, entityMetadata))
                .orElseThrow(() -> new UnsupportedOperationException("The repository does not support the method: " + method));
    }

    protected Object unwrapInvocationTargetException(ThrowingSupplier<Object> supplier) throws Throwable {
        try {
            return supplier.get();
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }

    /**
     * Creates a new {@link CustomRepositoryHandlerBuilder} instance.
     *
     * @return a {@link CustomRepositoryHandlerBuilder} instance
     */
    public static CustomRepositoryHandlerBuilder builder() {
        return new CustomRepositoryHandlerBuilder();
    }
}
