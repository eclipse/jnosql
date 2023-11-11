/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.document;


import jakarta.data.repository.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

record DefaultDocumentQuery(long limit, long skip, String name,
                            List<String> documents, List<Sort> sorts, DocumentCondition documentCondition) implements DocumentQuery {



    @Override
    public Optional<DocumentCondition> condition() {
        return ofNullable(documentCondition).map(DocumentCondition::readOnly);
    }

    @Override
    public List<Sort> sorts() {
        return unmodifiableList(sorts);
    }

    @Override
    public List<String> documents() {
        return unmodifiableList(documents);
    }

    static DocumentQuery countBy(DocumentQuery query) {
        return new DefaultDocumentQuery(0, 0, query.name(), query.documents(),
                Collections.emptyList(), query.condition().orElse(null));
    }
    static DocumentQuery existsBy(DocumentQuery query) {
        return new DefaultDocumentQuery(1, 0, query.name(), query.documents(),
                Collections.emptyList(), query.condition().orElse(null));
    }
}
