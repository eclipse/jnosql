/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.repository;

import jakarta.nosql.ServiceLoaderProvider;
import jakarta.nosql.mapping.PreparedStatement;
import org.eclipse.jnosql.mapping.reflection.RepositoryReflectionUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * The converter within the return method at Repository class.
 */
enum DynamicReturnConverter {

    INSTANCE;

    private final RepositoryReturn defaultReturn = new DefaultRepositoryReturn();

    /**
     * Converts the entity from the Method return type.
     *
     * @param dynamic the information about the method and return source
     * @return the conversion result
     * @throws NullPointerException when the dynamic is null
     */
    public Object convert(DynamicReturn<?> dynamic) {

        Method method = dynamic.getMethod();
        Class<?> typeClass = dynamic.typeClass();
        Class<?> returnType = method.getReturnType();

        RepositoryReturn repositoryReturn = ServiceLoaderProvider
                .getSupplierStream(RepositoryReturn.class)
                .filter(RepositoryReturn.class::isInstance)
                .map(RepositoryReturn.class::cast)
                .filter(r -> r.isCompatible(typeClass, returnType))
                .findFirst().orElse(defaultReturn);

        if (dynamic.hasPagination()) {
            return repositoryReturn.convertPageable(dynamic);
        } else {
            return repositoryReturn.convert(dynamic);
        }
    }

    /**
     * Reads and execute JNoSQL query from the Method that has the {@link jakarta.nosql.mapping.Query} annotation
     *
     * @return the result from the query annotation
     */
    public Object convert(DynamicQueryMethodReturn dynamicQueryMethod) {
        Method method = dynamicQueryMethod.getMethod();
        Object[] args = dynamicQueryMethod.getArgs();
        Function<String, Stream<?>> queryConverter = dynamicQueryMethod.getQueryConverter();
        Function<String, PreparedStatement> prepareConverter = dynamicQueryMethod.getPrepareConverter();
        Class<?> typeClass = dynamicQueryMethod.getTypeClass();

        String value = RepositoryReflectionUtils.INSTANCE.getQuery(method);


        Map<String, Object> params = RepositoryReflectionUtils.INSTANCE.getParams(method, args);
        Stream<?> entities;
        if (params.isEmpty()) {
            entities = queryConverter.apply(value);
        } else {
            PreparedStatement prepare = prepareConverter.apply(value);
            params.forEach(prepare::bind);
            entities = prepare.getResult();
        }

        Supplier<Stream<?>> streamSupplier = () -> entities;
        Supplier<Optional<?>> singleSupplier = DynamicReturn.toSingleResult(method).apply(streamSupplier);

        DynamicReturn dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(streamSupplier)
                .withSingleResult(singleSupplier)
                .build();

        return convert(dynamicReturn);
    }

}
