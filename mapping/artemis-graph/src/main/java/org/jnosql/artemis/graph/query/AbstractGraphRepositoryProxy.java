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
import org.jnosql.artemis.Param;
import org.jnosql.artemis.PreparedStatement;
import org.jnosql.artemis.Query;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.graph.GraphConverter;
import org.jnosql.artemis.graph.GraphTemplate;
import org.jnosql.artemis.query.RepositoryType;
import org.jnosql.artemis.reflection.ClassMapping;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.jnosql.artemis.graph.query.ReturnTypeConverterUtil.returnObject;

/**
 * Template method to {@link Repository} proxy on Graph
 *
 * @param <T>  the entity type
 * @param <ID> the ID entity
 */
abstract class AbstractGraphRepositoryProxy<T, ID> implements InvocationHandler {


    private final SelectQueryConverter converter = new SelectQueryConverter();
    private final DeleteQueryConverter deleteConverter = new DeleteQueryConverter();

    protected abstract ClassMapping getClassMapping();

    protected abstract Repository getRepository();

    protected abstract Graph getGraph();

    protected abstract GraphConverter getConverter();

    protected abstract GraphTemplate getTemplate();

    protected abstract Converters getConverters();


    @Override
    public Object invoke(Object instance, Method method, Object[] args) throws Throwable {
        String methodName = method.getName();
        RepositoryType type = RepositoryType.of(method);
        Class<?> typeClass = getClassMapping().getClassInstance();

        switch (type) {
            case DEFAULT:
                return method.invoke(getRepository(), args);
            case FIND_BY:
                return executeFindByMethod(method, args);
            case DELETE_BY:
                return executeDeleteMethod(method, args);
            case FIND_ALL:
                return executeFindAll(method, args);
            case OBJECT_METHOD:
                return method.invoke(this, args);
            case UNKNOWN:
            case JNOSQL_QUERY:
                return getJnosqlQuery(method, args, typeClass);
            default:
                return Void.class;

        }
    }

    private Object executeDeleteMethod(Method method, Object[] args) {

        GraphQueryMethod queryMethod = new GraphQueryMethod(getClassMapping(),
                getGraph().traversal().V(),
                getConverters(), method, args);

        List<Vertex> vertices = deleteConverter.apply(queryMethod);
        vertices.forEach(Vertex::remove);
        return Void.class;
    }

    private Object executeFindByMethod(Method method, Object[] args) {
        Class<?> classInstance = getClassMapping().getClassInstance();
        GraphQueryMethod queryMethod = new GraphQueryMethod(getClassMapping(),
                getGraph().traversal().V(),
                getConverters(), method, args);

        List<Vertex> vertices = converter.apply(queryMethod);
        Stream<T> stream = vertices.stream().map(getConverter()::toEntity);

        return returnObject(stream, classInstance, method);
    }

    private Object executeFindAll(Method method, Object[] args) {
        Class<?> classInstance = getClassMapping().getClassInstance();
        GraphTraversal<Vertex, Vertex> traversal = getGraph().traversal().V();
        List<Vertex> vertices = traversal.hasLabel(getClassMapping().getName()).toList();
        Stream<T> stream = vertices.stream().map(getConverter()::toEntity);
        return returnObject(stream, classInstance, method);
    }

    private Object getJnosqlQuery(Method method, Object[] args, Class<?> typeClass) {
        String value = method.getAnnotation(Query.class).value();
        Map<String, Object> params = getParams(method, args);
        List<T> entities;
        if (params.isEmpty()) {
            entities = getTemplate().query(value);
        } else {
            PreparedStatement prepare = getTemplate().prepare(value);
            params.forEach(prepare::bind);
            entities = prepare.getResultList();
        }
        return ReturnTypeConverterUtil.returnObject(entities, typeClass, method);
    }

    private Map<String, Object> getParams(Method method, Object[] args) {
        Map<String, Object> params = new HashMap<>();

        Parameter[] parameters = method.getParameters();
        for (int index = 0; index < parameters.length; index++) {
            Parameter parameter = parameters[index];
            Param param = parameter.getAnnotation(Param.class);
            if (Objects.nonNull(param)) {
                params.put(param.value(), args[index]);
            }
        }
        return params;
    }

}
