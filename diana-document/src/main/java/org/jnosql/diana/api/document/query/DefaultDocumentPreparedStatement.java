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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentPreparedStatement;
import org.jnosql.diana.api.document.DocumentQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class DefaultDocumentPreparedStatement implements DocumentPreparedStatement {

    private final DocumentEntity entity;

    private final DocumentQuery documentQuery;

    private final DocumentDeleteQuery documentDeleteQuery;

    private final PreparedStatementType type;

    private final Params params;

    private final String query;

    private final List<String> paramsLeft;

    private DefaultDocumentPreparedStatement(DocumentEntity entity, DocumentQuery documentQuery,
                                            DocumentDeleteQuery documentDeleteQuery,
                                            PreparedStatementType type, Params params,
                                            String query, List<String> paramsLeft) {
        this.entity = entity;
        this.documentQuery = documentQuery;
        this.documentDeleteQuery = documentDeleteQuery;
        this.type = type;
        this.params = params;
        this.query = query;
        this.paramsLeft = paramsLeft;
    }

    @Override
    public DocumentPreparedStatement bind(String name, Object value) {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(value, "value is required");

        paramsLeft.remove(name);
        return null;
    }

    @Override
    public List<DocumentEntity> getResultList() {
        return null;
    }

    @Override
    public Optional<DocumentEntity> getSingleResult() {
        return Optional.empty();
    }

    enum PreparedStatementType {
        SELECT, DELETE, UPDATE, INSERT
    }


    @Override
    public String toString() {
        return query;
    }
}
