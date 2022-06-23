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


import jakarta.nosql.mapping.PreparedStatement;
import org.eclipse.jnosql.mapping.reflection.MethodDynamicExecutable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * This instance has the information to run the JNoSQL native query at {@link jakarta.nosql.mapping.Repository}
 */
public final class DynamicQueryMethodReturn implements MethodDynamicExecutable {


    private final Method method;
    private final Object[] args;
    private final Class<?> typeClass;
    private final java.util.function.Function<String, Stream<?>> queryConverter;
    private final Function<String, PreparedStatement> prepareConverter;

    private DynamicQueryMethodReturn(Method method, Object[] args, Class<?> typeClass, Function<String, Stream<?>> queryConverter,
                                     Function<String, PreparedStatement> prepareConverter) {
        this.method = method;
        this.args = args;
        this.typeClass = typeClass;
        this.queryConverter = queryConverter;
        this.prepareConverter = prepareConverter;
    }

    Method getMethod() {
        return method;
    }

    Object[] getArgs() {
        return args;
    }

    Class<?> getTypeClass() {
        return typeClass;
    }

    Function<String, Stream<?>> getQueryConverter() {
        return queryConverter;
    }

    Function<String, PreparedStatement> getPrepareConverter() {
        return prepareConverter;
    }

    public static DynamicQueryMethodReturnBuilder builder() {
        return new DynamicQueryMethodReturnBuilder();
    }

    @Override
    public Object execute() {
        return DynamicReturnConverter.INSTANCE.convert(this);
    }

    public static final class DynamicQueryMethodReturnBuilder {

        private Method method;

        private Object[] args;

        private Class<?> typeClass;

        private Function<String, Stream<?>> queryConverter;

        private Function<String, PreparedStatement> prepareConverter;

        private DynamicQueryMethodReturnBuilder() {
        }

        public DynamicQueryMethodReturnBuilder withMethod(Method method) {
            this.method = method;
            return this;
        }

        public DynamicQueryMethodReturnBuilder withArgs(Object[] args) {
            if(args != null) {
                this.args = args.clone();
            }
            return this;
        }

        public DynamicQueryMethodReturnBuilder withTypeClass(Class<?> typeClass) {
            this.typeClass = typeClass;
            return this;
        }

        public DynamicQueryMethodReturnBuilder withQueryConverter(Function<String, Stream<?>> queryConverter) {
            this.queryConverter = queryConverter;
            return this;
        }

        public DynamicQueryMethodReturnBuilder withPrepareConverter(Function<String, PreparedStatement> prepareConverter) {
            this.prepareConverter = prepareConverter;
            return this;
        }
        public DynamicQueryMethodReturn build() {
            Objects.requireNonNull(method, "method is required");
            Objects.requireNonNull(typeClass, "typeClass is required");
            Objects.requireNonNull(queryConverter, "queryConverter is required");
            Objects.requireNonNull(prepareConverter, "prepareConverter is required");

            return new DynamicQueryMethodReturn(method, args, typeClass, queryConverter, prepareConverter);
        }
    }


}