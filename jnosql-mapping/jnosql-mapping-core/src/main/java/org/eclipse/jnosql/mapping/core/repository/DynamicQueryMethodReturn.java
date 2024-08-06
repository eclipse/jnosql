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
package org.eclipse.jnosql.mapping.core.repository;



import jakarta.data.page.Page;
import jakarta.data.page.PageRequest;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.mapping.PreparedStatement;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This instance has the information to run the JNoSQL native query at {@link jakarta.data.repository.CrudRepository}
 */
public final class DynamicQueryMethodReturn<T> implements MethodDynamicExecutable {


    private final Method method;
    private final Object[] args;
    private final Class<?> typeClass;
    private final Function<String, PreparedStatement> prepareConverter;
    private final Supplier<SelectQuery> querySupplier;
    private final PageRequest pageRequest;
    private final BiFunction<PageRequest, SelectQuery, Optional<T>> singleResultPagination;
    private final BiFunction<PageRequest, SelectQuery, Stream<T>> streamPagination;
    private final BiFunction<PageRequest, SelectQuery, Page<T>> page;

    private DynamicQueryMethodReturn(Method method, Object[] args, Class<?> typeClass,
                                     Function<String, PreparedStatement> prepareConverter,
                                     Supplier<SelectQuery> querySupplier,
                                     PageRequest pageRequest,
                                     BiFunction<PageRequest, SelectQuery, Optional<T>> singleResultPagination,
                                     BiFunction<PageRequest, SelectQuery, Stream<T>> streamPagination,
                                     BiFunction<PageRequest, SelectQuery, Page<T>> page) {
        this.method = method;
        this.args = args;
        this.typeClass = typeClass;
        this.prepareConverter = prepareConverter;
        this.querySupplier = querySupplier;
        this.pageRequest = pageRequest;
        this.singleResultPagination = singleResultPagination;
        this.streamPagination = streamPagination;
        this.page = page;
    }

    Method method() {
        return method;
    }

    Object[] args() {
        return args;
    }

    Class<?> typeClass() {
        return typeClass;
    }

    Function<String, PreparedStatement> prepareConverter() {
        return prepareConverter;
    }

    Supplier<SelectQuery> querySupplier() {
        return querySupplier;
    }

    PageRequest pageRequest() {
        return pageRequest;
    }

    BiFunction<PageRequest, SelectQuery, Optional<T>> singleResultPagination() {
        return singleResultPagination;
    }

    BiFunction<PageRequest, SelectQuery, Stream<T>> streamPagination() {
        return streamPagination;
    }

    BiFunction<PageRequest, SelectQuery, Page<T>> page() {
        return page;
    }

    public static <T> DynamicQueryMethodReturnBuilder<T> builder() {
        return new DynamicQueryMethodReturnBuilder<>();
    }

    @Override
    public Object execute() {
        return DynamicReturnConverter.INSTANCE.convert(this);
    }

    public static final class DynamicQueryMethodReturnBuilder<T> {

        private Method method;
        private Object[] args;
        private Class<?> typeClass;
        private Function<String, PreparedStatement> prepareConverter;
        private Supplier<SelectQuery> querySupplier;
        private PageRequest pageRequest;
        private BiFunction<PageRequest, SelectQuery, Optional<T>> singleResultPagination;
        private BiFunction<PageRequest, SelectQuery, Stream<T>> streamPagination;
        private  BiFunction<PageRequest, SelectQuery, Page<T>> page;

        private DynamicQueryMethodReturnBuilder() {
        }

        public DynamicQueryMethodReturnBuilder<T> withMethod(Method method) {
            this.method = method;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> withArgs(Object[] args) {
            if(args != null) {
                this.args = args.clone();
            }
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> withTypeClass(Class<?> typeClass) {
            this.typeClass = typeClass;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> withPrepareConverter(Function<String, PreparedStatement> prepareConverter) {
            this.prepareConverter = prepareConverter;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> querySupplier(Supplier<SelectQuery> querySupplier) {
            this.querySupplier = querySupplier;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> pageRequest(PageRequest pageRequest) {
            this.pageRequest = pageRequest;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> singleResultPagination(BiFunction<PageRequest, SelectQuery, Optional<T>> singleResultPagination) {
            this.singleResultPagination = singleResultPagination;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> streamPagination(BiFunction<PageRequest, SelectQuery, Stream<T>> streamPagination) {
            this.streamPagination = streamPagination;
            return this;
        }

        public DynamicQueryMethodReturnBuilder<T> page(BiFunction<PageRequest, SelectQuery, Page<T>> page) {
            this.page = page;
            return this;
        }

        public DynamicQueryMethodReturn<T> build() {
            Objects.requireNonNull(method, "method is required");
            Objects.requireNonNull(typeClass, "typeClass is required");
            Objects.requireNonNull(prepareConverter, "prepareConverter is required");
            return new DynamicQueryMethodReturn<>(method, args, typeClass, prepareConverter, querySupplier,
                    pageRequest, singleResultPagination, streamPagination, page);
        }
    }


}