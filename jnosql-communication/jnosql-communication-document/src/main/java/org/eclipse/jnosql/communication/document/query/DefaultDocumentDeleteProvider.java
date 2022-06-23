/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.document.DocumentDeleteQuery.DocumentDelete;
import jakarta.nosql.document.DocumentDeleteQuery.DocumentDeleteProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link DocumentDeleteProvider}
 */
public final class DefaultDocumentDeleteProvider implements DocumentDeleteProvider {

    @Override
    public DocumentDelete apply(String[] documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        return new DefaultFluentDeleteQueryBuilder(Arrays.asList(documents));
    }

    @Override
    public DocumentDelete get() {
        return new DefaultFluentDeleteQueryBuilder(Collections.emptyList());
    }
}
