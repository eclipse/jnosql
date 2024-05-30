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


import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.core.query.AnnotationOperation;
import org.eclipse.jnosql.mapping.core.query.RepositoryType;
import org.eclipse.jnosql.mapping.core.repository.ThrowingSupplier;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.semistructured.SemiStructuredTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.logging.Logger;

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
    public Object invoke(Object proxy, Method method, Object[] params) throws Throwable {
        LOGGER.info("Executing the method: " + method);
        RepositoryType type = RepositoryType.of(method, customRepositoryType);
        switch (type) {
            case SAVE -> {
                return unwrapInvocationTargetException(() -> SAVE.invoke(new AnnotationOperation.Operation(method, params, repository(method))));
            }
            case INSERT -> {
                return unwrapInvocationTargetException(() -> INSERT.invoke(new AnnotationOperation.Operation(method, params, repository(method))));
            }
            case DELETE -> {
                return unwrapInvocationTargetException(() -> DELETE.invoke(new AnnotationOperation.Operation(method, params, repository(method))));
            }
            case UPDATE -> {
                return unwrapInvocationTargetException(() -> UPDATE.invoke(new AnnotationOperation.Operation(method, params, repository(method))));
            }
            default -> {
                return Void.class;
            }
        }
    }

    private AbstractRepository<?,?> repository(Method method) {
        Class<?> typeClass = (Class<?>) method.getReturnType();
        if (typeClass.isArray()) {
            typeClass = typeClass.getComponentType();
        } else if(Iterable.class.isAssignableFrom(typeClass)) {
            typeClass = (Class<?>) ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0];
        }
        Optional<EntityMetadata> entity = entitiesMetadata.findByClassName(typeClass.getName());
        return entity.map(entityMetadata -> new SemiStructuredRepositoryProxy.SemiStructuredRepository<>(template, entityMetadata)).orElse(null);
    }

    protected Object unwrapInvocationTargetException(ThrowingSupplier<Object> supplier) throws Throwable {
        try {
            return supplier.get();
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
