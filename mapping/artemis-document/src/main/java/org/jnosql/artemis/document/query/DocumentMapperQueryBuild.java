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
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;


/**
 * The last step to the build of {@link org.jnosql.diana.api.document.DocumentQuery}.
 * It either can return a new {@link org.jnosql.diana.api.document.DocumentQuery} instance or execute a query with
 * {@link DocumentTemplate}
 * and {@link DocumentTemplateAsync}
 */
public interface DocumentMapperQueryBuild {

    /**
     * Creates a new instance of {@link DocumentQuery}
     *
     * @return a new {@link DocumentQuery} instance
     */
    DocumentQuery build();

    /**
     * Executes {@link DocumentTemplate#select(DocumentQuery)}
     *
     * @param <T>      the entity type
     * @param template the template to document
     * @return the result of {@link DocumentTemplate#select(DocumentQuery)}
     * @throws NullPointerException when manager is null
     */
    <T> List<T> execute(DocumentTemplate template);

    /**
     * Executes {@link DocumentTemplate#singleResult(DocumentQuery)}
     *
     * @param <T>      the entity type
     * @param template the template to document
     * @return the result of {@link DocumentTemplate#singleResult(DocumentQuery)}
     * @throws NullPointerException when manager is null
     */
    <T> Optional<T> executeSingle(DocumentTemplate template);

    /**
     * Executes {@link DocumentTemplateAsync#select(DocumentQuery, Consumer)}
     *
     * @param <T>           the entity type
     * @param templateAsync the templateAsync
     * @param callback      the callback
     * @throws NullPointerException when there is null parameter
     */
    <T> void execute(DocumentTemplateAsync templateAsync, Consumer<List<T>> callback);

    /**
     * Executes {@link DocumentTemplateAsync#singleResult(DocumentQuery, Consumer)}
     *
     * @param <T>           the entity type
     * @param templateAsync the templateAsync
     * @param callback      the callback
     * @throws NullPointerException when there is null parameter
     */
    <T> void executeSingle(DocumentTemplateAsync templateAsync, Consumer<Optional<T>> callback);

}