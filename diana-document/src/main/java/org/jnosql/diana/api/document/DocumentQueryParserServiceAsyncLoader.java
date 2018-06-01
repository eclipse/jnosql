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
package org.jnosql.diana.api.document;

import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

final class DocumentQueryParserServiceAsyncLoader {

    private static final List<DocumentQueryParserAsync> LOADERS;

    static final Optional<DocumentQueryParserAsync> INSTANCE;

    private static final String MESSAGE = "Could not found an implementation of DocumentQueryParserAsync in service loader.";

    static {
        ServiceLoader<DocumentQueryParserAsync> serviceLoader = ServiceLoader.load(DocumentQueryParserAsync.class);
        LOADERS = StreamSupport.stream(serviceLoader.spliterator(), false).collect(toList());
        INSTANCE = LOADERS.stream().findFirst();
    }

    private DocumentQueryParserServiceAsyncLoader() {
    }

    static DocumentQueryParserAsync getInstance() {
        return INSTANCE.orElseThrow(() -> new IllegalStateException(MESSAGE));
    }
}
