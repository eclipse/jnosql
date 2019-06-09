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


import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.diana.column.ColumnDeleteQuery;

import java.util.function.Consumer;

/**
 * The last step to the build of {@link ColumnDeleteQuery}.
 * It either can return a new {@link ColumnDeleteQuery} instance or execute a query with
 * {@link ColumnTemplate} and {@link org.jnosql.artemis.column.ColumnTemplateAsync}
 */
public interface ColumnMapperDeleteQueryBuild {

    /**
     * Creates a new instance of {@link ColumnDeleteQuery}
     *
     * @return a new {@link ColumnDeleteQuery} instance
     */
    ColumnDeleteQuery build();

    /**
     * executes the {@link ColumnTemplate#delete(ColumnDeleteQuery)}
     *
     * @param template the column template
     * @throws NullPointerException when manager is null
     */
    void execute(ColumnTemplate template);

    /**
     * executes the {@link ColumnTemplateAsync#delete(ColumnDeleteQuery)}
     *
     * @param template the column template
     * @throws NullPointerException when manager is null
     */
    void execute(ColumnTemplateAsync template);

    /**
     * executes the {@link ColumnTemplateAsync#delete(ColumnDeleteQuery, Consumer)}
     *
     * @param template the column template
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void execute(ColumnTemplateAsync template, Consumer<Void> callback);

}
