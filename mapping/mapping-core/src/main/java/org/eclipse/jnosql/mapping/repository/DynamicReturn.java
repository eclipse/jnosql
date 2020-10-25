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

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Sort;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Sorts;
import org.eclipse.jnosql.mapping.reflection.MethodDynamicExecutable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * This instance has information to return at the dynamic query in Repository.
 * To create an instance, use, {@link DynamicReturn#builder()}
 *
 * @param <T> the source type
 */
public final class DynamicReturn<T> implements MethodDynamicExecutable {

    /**
     * A wrapper function that convert a result as a list to a result as optional
     *
     * @param method the method source
     * @return the function that does this conversion
     */
    public static Function<Supplier<Stream<?>>, Supplier<Optional<?>>> toSingleResult(final Method method) {
        return new SupplierConverter(method);
    }

    /**
     * A predicate to check it the object is instance of {@link Pagination}
     */
    private static final Predicate<Object> IS_PAGINATION = Pagination.class::isInstance;

    /**
     * Finds {@link Pagination} from array object
     *
     * @param params the params
     * @return a {@link Pagination} or null
     */
    public static Pagination findPagination(Object[] params) {
        if (params == null || params.length == 0) {
            return null;
        }
        return Stream.of(params)
                .filter(IS_PAGINATION)
                .map(Pagination.class::cast)
                .findFirst().orElse(null);
    }

    /**
     * Finds {@link Sort} and {@link Sorts} at the parameter array
     *
     * @param params the params
     * @return a list of {@link Sort} found
     */
    public static List<Sort> findSorts(Object[] params) {
        if (params == null || params.length == 0) {
            return Collections.emptyList();
        }

        List<Sort> sorts = new ArrayList<>();
        for (Object param : Arrays.asList(params)) {
            if (param instanceof Sort) {
                sorts.add(Sort.class.cast(param));
            } else if (param instanceof Sorts) {
                sorts.addAll(Sorts.class.cast(param).getSorts());
            }
        }
        return sorts;
    }

    @Override
    public Object execute() {
        return DynamicReturnConverter.INSTANCE.convert(this);
    }

    private static class SupplierConverter implements Function<Supplier<Stream<?>>, Supplier<Optional<?>>> {

        private final Method method;

        SupplierConverter(Method method) {
            this.method = method;
        }

        @Override
        public Supplier<Optional<?>> apply(Supplier<Stream<?>> supplier) {
            return () -> {
                Stream<?> entities = supplier.get();
                final Iterator<?> iterator = entities.iterator();
                if (!iterator.hasNext()) {
                    return Optional.empty();
                }
                final Object entity = iterator.next();
                if (!iterator.hasNext()) {
                    return Optional.ofNullable(entity);
                }
                throw new NonUniqueResultException("No unique result to the method: " + method);
            };
        }
    }


    private final Class<T> classSource;

    private final Method methodSource;

    private final Supplier<Optional<T>> singleResult;

    private final Supplier<Stream<T>> result;

    private final Pagination pagination;

    private final Function<Pagination, Optional<T>> singleResultPagination;

    private final Function<Pagination, Stream<T>> streamPagination;

    private final Function<Pagination, Page<T>> page;

    private DynamicReturn(Class<T> classSource, Method methodSource,
                          Supplier<Optional<T>> singleResult,
                          Supplier<Stream<T>> result, Pagination pagination,
                          Function<Pagination, Optional<T>> singleResultPagination,
                          Function<Pagination, Stream<T>> streamPagination,
                          Function<Pagination, Page<T>> page) {
        this.classSource = classSource;
        this.methodSource = methodSource;
        this.singleResult = singleResult;
        this.result = result;
        this.pagination = pagination;
        this.singleResultPagination = singleResultPagination;
        this.streamPagination = streamPagination;
        this.page = page;
    }

    /**
     * The repository class type source.
     *
     * @return The repository class type source.
     */
    public Class<T> typeClass() {
        return classSource;
    }

    /**
     * The method source at the Repository
     *
     * @return The method source at the Repository
     */
    public Method getMethod() {
        return methodSource;
    }

    /**
     * Returns the result as single result
     *
     * @return the result as single result
     */
    public Optional<T> singleResult() {
        return singleResult.get();
    }

    /**
     * Returns the result as {@link List}
     *
     * @return the result as {@link List}
     */
    public Stream<T> result() {
        return result.get();
    }

    /**
     * @return the pagination
     */
    Optional<Pagination> getPagination() {
        return Optional.ofNullable(pagination);
    }

    /**
     * @return returns a single result with pagination
     */
    public Optional<T> singleResultPagination() {
        return singleResultPagination.apply(pagination);
    }

    /**
     * @return a list result using pagination
     */
    public Stream<T> streamPagination() {
        return streamPagination.apply(pagination);
    }

    /**
     * @return the page
     */
    public Page<T> getPage() {
        return page.apply(pagination);
    }

    /**
     * @return check if there is pagination
     */
    boolean hasPagination() {
        return pagination != null;
    }

    /**
     * Creates a builder to DynamicReturn
     *
     * @param <T> the type
     * @return a builder instance
     */
    public static <T> DefaultDynamicReturnBuilder builder() {
        return new DefaultDynamicReturnBuilder();
    }

    /**
     * A builder of {@link DynamicReturn}
     */
    public static final class DefaultDynamicReturnBuilder<T> {

        private Class<?> classSource;

        private Method methodSource;

        private Supplier<Optional<T>> singleResult;

        private Supplier<Stream<T>> result;

        private Pagination pagination;

        private Function<Pagination, Optional<T>> singleResultPagination;

        private Function<Pagination, Stream<T>> streamPagination;

        private Function<Pagination, Page<T>> page;

        private DefaultDynamicReturnBuilder() {
        }

        /**
         * @param classSource set the classSource
         * @return the instance
         */
        public DefaultDynamicReturnBuilder withClassSource(Class<?> classSource) {
            this.classSource = classSource;
            return this;
        }

        /**
         * @param methodSource the method source
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withMethodSource(Method methodSource) {
            this.methodSource = methodSource;
            return this;
        }

        /**
         * @param singleResult the singleResult source
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withSingleResult(Supplier<Optional<T>> singleResult) {
            this.singleResult = singleResult;
            return this;
        }

        /**
         * @param result the list
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withResult(Supplier<Stream<T>> result) {
            this.result = result;
            return this;
        }

        /**
         * @param pagination the pagination
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withPagination(Pagination pagination) {
            this.pagination = pagination;
            return this;
        }

        /**
         * @param singleResultPagination the single result pagination
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withSingleResultPagination(Function<Pagination, Optional<T>> singleResultPagination) {
            this.singleResultPagination = singleResultPagination;
            return this;
        }

        /**
         * @param listPagination the list pagination
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withStreamPagination(Function<Pagination, Stream<T>> listPagination) {
            this.streamPagination = listPagination;
            return this;
        }

        /**
         * @param page the page
         * @return the builder instance
         */
        public DefaultDynamicReturnBuilder withPage(Function<Pagination, Page<T>> page) {
            this.page = page;
            return this;
        }

        /**
         * Creates a {@link DynamicReturn} from the parameters, all fields are required
         *
         * @return a new instance
         * @throws NullPointerException when there is null attributes
         */
        public DynamicReturn build() {
            requireNonNull(classSource, "the class Source is required");
            requireNonNull(methodSource, "the method Source is required");
            requireNonNull(singleResult, "the single result supplier is required");
            requireNonNull(result, "the result supplier is required");

            if (pagination != null) {
                requireNonNull(singleResultPagination, "singleResultPagination is required when pagination is not null");
                requireNonNull(streamPagination, "listPagination is required when pagination is not null");
                requireNonNull(page, "page is required when pagination is not null");
            }

            return new DynamicReturn(classSource, methodSource, singleResult, result,
                    pagination, singleResultPagination, streamPagination, page);
        }
    }

}
