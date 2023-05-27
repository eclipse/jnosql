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

import jakarta.data.exceptions.MappingException;
import jakarta.data.repository.Page;
import jakarta.data.repository.Pageable;
import jakarta.data.repository.PageableRepository;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.NoSQLPage;
import org.eclipse.jnosql.mapping.graph.GraphConverter;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.query.RepositoryType;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.repository.DynamicQueryMethodReturn;
import org.eclipse.jnosql.mapping.repository.DynamicReturn;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Template method to {@link PageableRepository} proxy on Graph
 *
 * @param <T> the entity type
 * @param <K> the K entity
 */
abstract class AbstractGraphRepositoryProxy<T, K> implements InvocationHandler {


    protected abstract EntityMetadata getEntityMetadata();

    protected abstract PageableRepository getRepository();

    protected abstract Graph getGraph();

    protected abstract GraphConverter getConverter();

    protected abstract GraphTemplate getTemplate();

    protected abstract Converters getConverters();


    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getEntityMetadata().type();

        switch (type) {
            case DEFAULT -> {
                return method.invoke(getRepository(), args);
            }
            case FIND_BY -> {
                return findBy(method, args, typeClass);
            }
            case FIND_ALL -> {
                return findAll(method, typeClass, args);
            }
            case DELETE_BY -> {
                return executeDeleteMethod(method, args);
            }
            case OBJECT_METHOD -> {
                return method.invoke(this, args);
            }
            case COUNT_BY -> {
                return countBy(method, args);
            }
            case EXISTS_BY -> {
                return existsBy(method, args);
            }
            case DEFAULT_METHOD -> {
                return InvocationHandler.invokeDefault(instance, method, args);
            }
            case ORDER_BY ->
                    throw new MappingException("Eclipse JNoSQL has not support for method that has OrderBy annotation");
            case QUERY -> {
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q))
                        .withQueryConverter(q -> getTemplate().query(q)).build();
                return methodReturn.execute();
            }
            default -> {
                return Void.class;
            }
        }
    }

    private Object findAll(Method method, Class<?> typeClass, Object[] args) {

        Supplier<Stream<?>> querySupplier = () -> {

            GraphTraversal<Vertex, Vertex> traversal = getGraph().traversal().V().hasLabel(getEntityMetadata().name());

            SelectQueryConverter.updateDynamicParameter(args, traversal, getEntityMetadata());
            return traversal.toStream()
                    .map(getConverter()::toEntity);
        };

        return converter(method, typeClass, querySupplier, args);
    }

    private Object existsBy(Method method, Object[] args) {
        Long countBy = (Long) countBy(method, args);
        return countBy > 0;
    }

    private Object countBy(Method method, Object[] args) {

        Supplier<Long> querySupplier = () -> {
            GraphQueryMethod queryMethod = new GraphQueryMethod(getEntityMetadata(),
                    getGraph().traversal().V(),
                    getConverters(), method, args);
            return CountQueryConverter.INSTANCE.apply(queryMethod, args);
        };

        return querySupplier.get();
    }

    private Object findBy(Method method, Object[] args, Class<?> typeClass) {

        Supplier<Stream<?>> querySupplier = () -> {
            GraphQueryMethod queryMethod = new GraphQueryMethod(getEntityMetadata(),
                    getGraph().traversal().V(),
                    getConverters(), method, args);

            return SelectQueryConverter.INSTANCE.apply(queryMethod, args)
                    .map(getConverter()::toEntity);
        };

        return converter(method, typeClass, querySupplier, args);
    }

    private Object converter(Method method, Class<?> typeClass,
                             Supplier<Stream<?>> querySupplier,
                             Object[] args) {

        Supplier<Optional<?>> singleSupplier =
                DynamicReturn.toSingleResult(method).apply(querySupplier);

        Function<Pageable, Page<?>> pageFunction = p -> {
            List<?> entities = querySupplier.get().toList();
            return NoSQLPage.of(entities, p);
        };

        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withResult(querySupplier)
                .withSingleResult(singleSupplier)
                .withPagination(DynamicReturn.findPageable(args))
                .withStreamPagination(p -> querySupplier.get())
                .withSingleResultPagination(p -> singleSupplier.get())
                .withPage(pageFunction)
                .build();

        return dynamicReturn.execute();
    }

    private Object executeDeleteMethod(Method method, Object[] args) {

        GraphQueryMethod queryMethod = new GraphQueryMethod(getEntityMetadata(),
                getGraph().traversal().V(),
                getConverters(), method, args);

        List<Vertex> vertices = DeleteQueryConverter.INSTANCE.apply(queryMethod);
        vertices.forEach(Vertex::remove);
        return Void.class;
    }

}
