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

class MappingDocumentDeleteQuery implements DocumentDeleteQuery {

    private final String documentCollection;

    private final DocumentCondition condition;

    MappingDocumentDeleteQuery(String documentCollection, DocumentCondition condition) {
        this.documentCollection = documentCollection;
        this.condition = condition;
    }

    @Override
    public String documentCollection() {
        return documentCollection;
    }

    @Override
    public Optional<DocumentCondition> condition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<String> documents() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDeleteQuery)) {
            return false;
        }
        DocumentDeleteQuery that = (DocumentDeleteQuery) o;
        return Objects.equals(documentCollection, that.documentCollection()) &&
                Objects.equals(condition, that.condition().orElse(null)) && Objects.equals(Collections.emptyList(),
                that.documents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentCollection, condition, Collections.emptyList());
    }

    @Override
    public String toString() {
        return  "ArtemisDocumentDeleteQuery{" + "documentCollection='" + documentCollection + '\'' +
                ", condition=" + condition +
                ", documents=" + Collections.emptyList() +
                '}';
    }
}
