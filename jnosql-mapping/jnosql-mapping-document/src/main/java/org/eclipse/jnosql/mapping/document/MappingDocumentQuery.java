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
package org.eclipse.jnosql.mapping.document;

import jakarta.data.repository.Sort;
import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The mapping implementation of {@link DocumentQuery}
 */
public record MappingDocumentQuery(List<Sort> sorts, long limit, long skip, DocumentCondition documentCondition,
                                   String documentCollection) implements DocumentQuery {


    @Override
    public String name() {
        return documentCollection;
    }

    @Override
    public Optional<DocumentCondition> condition() {
        return Optional.ofNullable(documentCondition);
    }

    @Override
    public List<Sort> sorts() {
        return Collections.unmodifiableList(sorts);
    }

    @Override
    public List<String> documents() {
        return Collections.emptyList();
    }

}

