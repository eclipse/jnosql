/*
 *
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
 *
 */
package org.jnosql.artemis.column.query;

import org.jnosql.artemis.Page;
import org.jnosql.artemis.Pagination;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The last step to the build of {@link ColumnQuery}.
 * It either can return a new {@link ColumnQuery} instance or execute a query with
 * {@link org.jnosql.artemis.column.ColumnTemplate} and {@link org.jnosql.artemis.column.ColumnTemplateAsync}
 */
public interface ColumnMapperQueryBuild {

    /**
     * Creates a new instance of {@link ColumnQuery}
     *
     * @return a new {@link ColumnQuery} instance
     */
    ColumnQuery build();

    /**
     * Creates a new instance of {@link ColumnQuery} from {@link Pagination}
     *
     * @param pagination the pagination
     * @return a new {@link ColumnQuery} instance from {@link Pagination}
     */
    ColumnQuery build(Pagination pagination);

    /**
     * Executes {@link ColumnTemplate#select(ColumnQuery)}
     *
     * @param <T>      the entity type
     * @param template the column template
     * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
     * @throws NullPointerException when manager is null
     */
    <T> List<T> execute(ColumnTemplate template);

    /**
     * Executes {@link ColumnTemplate#singleResult(ColumnQuery)}
     *
     * @param <T>      the entity type
     * @param template the column template
     * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
     * @throws NullPointerException when manager is null
     */
    <T> Optional<T> executeSingle(ColumnTemplate template);

    /**
     * Executes {@link ColumnTemplate#select(ColumnQuery)} using {@link Pagination}
     *
     * @param <T>        the entity type
     * @param template   the column template
     * @param pagination the pagination
     * @return the result of {@link ColumnTemplate#select(ColumnQuery)}
     * @throws NullPointerException when there are null parameters
     */
    <T> List<T> execute(ColumnTemplate template, Pagination pagination);

    /**
     * Executes {@link ColumnTemplate#singleResult(ColumnQuery)} using {@link Pagination}
     *
     * @param <T>        the entity type
     * @param template   the column template
     * @param pagination the pagination
     * @return the result of {@link ColumnTemplate#singleResult(ColumnQuery)}
     * @throws NullPointerException when there are null parameters
     */
    <T> Optional<T> executeSingle(ColumnTemplate template, Pagination pagination);


    /**
     * Executes {@link ColumnTemplateAsync#select(ColumnQuery, Consumer)}
     *
     * @param <T>      the entity type
     * @param template the column template
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    <T> void execute(ColumnTemplateAsync template, Consumer<List<T>> callback);

    /**
     * Executes {@link ColumnTemplateAsync#singleResult(ColumnQuery, Consumer)}
     *
     * @param <T>      the entity type
     * @param template the column template
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    <T> void executeSingle(ColumnTemplateAsync template, Consumer<Optional<T>> callback);

    /**
     * Creates a {@link Page} from pagination
     *
     * @param pagination the pagination
     * @param template   the template
     * @param <T>        the type
     * @return a {@link Page} from instance
     * @throws NullPointerException when there are null parameters
     */
    <T> Page<T> page(Pagination pagination, ColumnTemplate template);
}
