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

import org.eclipse.jnosql.communication.document.DocumentCondition;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

record MappingDocumentDeleteQuery(String documentCollection, DocumentCondition documentCondition) implements DocumentDeleteQuery {

    @Override
    public String name() {
        return documentCollection;
    }

    @Override
    public Optional<DocumentCondition> condition() {
        return Optional.ofNullable(documentCondition);
    }

    @Override
    public List<String> documents() {
        return Collections.emptyList();
    }
}
