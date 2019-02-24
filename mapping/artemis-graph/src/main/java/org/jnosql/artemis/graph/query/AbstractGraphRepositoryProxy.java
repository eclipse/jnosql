/*
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.artemis.graph.query;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.graph.GraphConverter;
import org.jnosql.artemis.graph.GraphTemplate;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.DynamicQueryMethodReturn;
import org.jnosql.artemis.reflection.DynamicReturn;
import org.jnosql.artemis.reflection.DynamicReturnConverter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * Template method to {@link Repository} proxy on Graph
 *
 * @param <T>  the entity type
 * @param <ID> the ID entity
 */
abstract class AbstractGraphRepositoryProxy<T, ID> implements InvocationHandler {


    private final SelectQueryConverter converter = new SelectQueryConverter();

    private final DeleteQueryConverter deleteConverter = new DeleteQueryConverter();

    private final DynamicReturnConverter returnConverter = DynamicReturnConverter.INSTANCE;

    protected abstract ClassMapping getClassMapping();

    protected abstract Repository getRepository();

    protected abstract Graph getGraph();

    protected abstract GraphConverter getConverter();

    protected abstract GraphTemplate getTemplate();

    protected abstract Converters getConverters();


    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getClassInstance();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                return findById(method, args, typeClass);
            case DELETE_BY:
                return executeDeleteMethod(method, args);
            case FIND_ALL:
                return findAll(method, typeClass);
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case UNKNOWN:
            case JNOSQL_QUERY:
                DynamicQueryMethodReturn methodReturn = DynamicQueryMethodReturn.builder()
                        .withArgs(args)
                        .withMethod(method)
                        .withTypeClass(typeClass)
                        .withPrepareConverter(q -> getTemplate().prepare(q))
                        .withQueryConverter(q -> getTemplate().query(q)).build();
                return returnConverter.convert(methodReturn);
            default:
                return Void.class;

        }
    }

    private Object findAll(Method method, Class<?> typeClass) {
        GraphTraversal<Vertex, Vertex> traversal = getGraph().traversal().V();
        List<Vertex> vertices = traversal.hasLabel(getClassMapping().getName()).toList();
        Stream<T> stream = vertices.stream().map(getConverter()::toEntity);
        Supplier<List<?>> querySupplier = () ->
                traversal.hasLabel(getClassMapping().getName()).toList().stream()
                        .map(getConverter()::toEntity).collect(toList());

        return converter(method, typeClass, querySupplier);
    }

    private Object findById(Method method, Object[] args, Class<?> typeClass) {
        GraphQueryMethod queryMethod = new GraphQueryMethod(getClassMapping(),
                getGraph().traversal().V(),
                getConverters(), method, args);

        Supplier<List<?>> querySupplier = () -> converter.apply(queryMethod)
                .stream()
                .map(getConverter()::toEntity)
                .collect(toList());

        return converter(method, typeClass, querySupplier);
    }

    private Object converter(Method method, Class<?> typeClass, Supplier<List<?>> querySupplier) {
        Supplier<Optional<?>> singleSupplier =
                DynamicReturn.toSingleResult(method).apply(querySupplier);

        DynamicReturn<?> dynamicReturn = DynamicReturn.builder()
                .withClassSource(typeClass)
                .withMethodSource(method)
                .withList(querySupplier)
                .withSingleResult(singleSupplier)
                .build();

        return returnConverter.convert(dynamicReturn);
    }

    private Object executeDeleteMethod(Method method, Object[] args) {

        GraphQueryMethod queryMethod = new GraphQueryMethod(getClassMapping(),
                getGraph().traversal().V(),
                getConverters(), method, args);

        List<Vertex> vertices = deleteConverter.apply(queryMethod);
        vertices.forEach(Vertex::remove);
        return Void.class;
    }

}
