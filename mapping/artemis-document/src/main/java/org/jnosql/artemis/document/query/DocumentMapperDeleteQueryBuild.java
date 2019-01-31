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
package org.jnosql.artemis.document.query;


import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.diana.api.document.DocumentDeleteQuery;

import java.util.function.Consumer;

/**
 * The last step to the build of {@link DocumentDeleteQuery}.
 * It either can return a new {@link DocumentDeleteQuery} instance or execute a query with
 * {@link org.jnosql.artemis.document.DocumentTemplate} and {@link org.jnosql.artemis.document.DocumentTemplateAsync}
 */
public interface DocumentMapperDeleteQueryBuild {
    /**
     * Creates a new instance of {@link DocumentDeleteQuery}
     *
     * @return a new {@link DocumentDeleteQuery} instance
     */
    DocumentDeleteQuery build();

    /**
     * executes the {@link DocumentTemplate#delete(DocumentDeleteQuery)}
     *
     * @param template the document template
     * @throws NullPointerException when manager is null
     */
    void execute(DocumentTemplate template);

    /**
     * executes the {@link DocumentTemplateAsync#delete(DocumentDeleteQuery)}
     *
     * @param template the document template
     * @throws NullPointerException when manager is null
     */
    void execute(DocumentTemplateAsync template);

    /**
     * executes the {@link DocumentTemplateAsync#delete(DocumentDeleteQuery, Consumer)}
     *
     * @param template the document template
     * @param callback the callback
     * @throws NullPointerException when there is null parameter
     */
    void execute(DocumentTemplateAsync template, Consumer<Void> callback);
}
