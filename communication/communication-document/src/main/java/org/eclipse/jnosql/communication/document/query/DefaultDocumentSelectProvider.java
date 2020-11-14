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

import jakarta.nosql.document.DocumentQuery.DocumentSelect;
import jakarta.nosql.document.DocumentQuery.DocumentSelectProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link DocumentSelectProvider}
 */
public final class DefaultDocumentSelectProvider implements DocumentSelectProvider {

    @Override
    public DocumentSelect apply(String[] documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        return new DefaultSelectQueryBuilder(Arrays.asList(documents));
    }

    @Override
    public DocumentSelect get() {
        return new DefaultSelectQueryBuilder(Collections.emptyList());
    }
}
