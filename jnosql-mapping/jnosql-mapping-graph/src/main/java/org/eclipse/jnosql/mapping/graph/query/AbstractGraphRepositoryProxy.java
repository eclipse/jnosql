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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.NoSQLPage;
import org.eclipse.jnosql.mapping.core.query.AbstractRepositoryProxy;
import org.eclipse.jnosql.mapping.core.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;
import org.eclipse.jnosql.mapping.core.repository.RepositoryReflectionUtils;
import org.eclipse.jnosql.mapping.graph.GraphConverter;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Template method to {@link org.eclipse.jnosql.mapping.NoSQLRepository} proxy on Graph
 *
 * @param <T> the entity type
 * @param <K> the K entity
 */
abstract class AbstractGraphRepositoryProxy<T, K> extends AbstractRepositoryProxy<T, K> {

    protected abstract Graph graph();

    protected abstract GraphConverter converter();

    protected abstract GraphTemplate template();

    protected abstract Converters converters();

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
        return executeDeleteMethod(method, params);
    }

    @Override
    protected Object executeFindAll(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        return findAll(method, type, params);
    }

    @Override
    protected Object executeExistByQuery(Object instance, Method method, Object[] params) {
        return existsBy(method, params);
    }

    @Override
    protected Object executeCountByQuery(Object instance, Method method, Object[] params) {
        return countBy(method, params);
    }

    @Override
    protected Object executeFindByQuery(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        return findBy(method, params, type);
    }


    @Override
    protected Object executeParameterBased(Object instance, Method method, Object[] params) {
        Class<?> type = entityMetadata().type();
        Map<String, Object> parameters = RepositoryReflectionUtils.INSTANCE.getBy(method, params);
        var methodName = "findBy" + parameters.keySet().stream()
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining("And"));
        Supplier<Stream<?>> querySupplier = () -> {
            GraphQueryMethod queryMethod = new GraphQueryMethod(entityMetadata(),
                    graph().traversal().V(),
                    converters(), null, methodName, params);

            return SelectQueryConverter.INSTANCE.apply(queryMethod, params)
                    .map(converter()::toEntity);
        };

        return converter(method, type, querySupplier, params);
    }


    private Object findAll(Method method, Class<?> typeClass, Object[] args) {

        Supplier<Stream<?>> querySupplier = () -> {

            GraphTraversal<Vertex, Vertex> traversal = graph().traversal().V().hasLabel(entityMetadata().name());

            SelectQueryConverter.updateDynamicParameter(args, traversal, entityMetadata());
            return traversal.toStream()
                    .map(converter()::toEntity);
        };

        return converter(method, typeClass, querySupplier, args);
    }

    private Object existsBy(Method method, Object[] args) {
        Long countBy = (Long) countBy(method, args);
        return countBy > 0;
    }

    private Object countBy(Method method, Object[] args) {

        Supplier<Long> querySupplier = () -> {
            GraphQueryMethod queryMethod = new GraphQueryMethod(entityMetadata(),
                    graph().traversal().V(),
                    converters(), method, args);
            return CountQueryConverter.INSTANCE.apply(queryMethod, args);
        };

        return querySupplier.get();
    }

    private Object findBy(Method method, Object[] args, Class<?> typeClass) {

        Supplier<Stream<?>> querySupplier = () -> {
            GraphQueryMethod queryMethod = new GraphQueryMethod(entityMetadata(),
                    graph().traversal().V(),
                    converters(), method, args);

            return SelectQueryConverter.INSTANCE.apply(queryMethod, args)
                    .map(converter()::toEntity);
        };

        return converter(method, typeClass, querySupplier, args);
    }

    private Object converter(Method method, Class<?> typeClass,
                             Supplier<Stream<?>> querySupplier,
                             Object[] args) {

        Supplier<Optional<?>> singleSupplier =
                DynamicReturn.toSingleResult(method).apply(querySupplier);

        Function<PageRequest, Page<?>> pageFunction = p -> {
            List<?> entities = querySupplier.get().toList();
            return NoSQLPage.of(entities, p);
        };

        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(querySupplier)
                .withSingleResult(singleSupplier)
                .withPagination(DynamicReturn.findPageRequest(args))
                .withStreamPagination(p -> querySupplier.get())
                .withSingleResultPagination(p -> singleSupplier.get())
                .withPage(pageFunction)
                .build();

        return dynamicReturn.execute();
    }

    private Object executeDeleteMethod(Method method, Object[] args) {

        GraphQueryMethod queryMethod = new GraphQueryMethod(entityMetadata(),
                graph().traversal().V(),
                converters(), method, args);

        List<Vertex> vertices = DeleteQueryConverter.INSTANCE.apply(queryMethod);
        vertices.forEach(Vertex::remove);
        return Void.class;
    }

}
