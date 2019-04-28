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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.Params;
import org.jnosql.diana.api.QueryException;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentObserverParser;
import org.jnosql.query.JSONValue;

import java.util.List;


abstract class ConditionQueryParser {

    protected DocumentEntity getEntity(ConditionQuerySupplier query, String collection, Params params, DocumentObserverParser observer) {
        DocumentEntity entity = DocumentEntity.of(collection);

        if (query.useJSONCondition()) {
            JSONValue jsonValue = query.getValue().orElseThrow(() -> new QueryException(""));
            List<Document> documents = JsonObjects.getDocuments(jsonValue.get());
            entity.addAll(documents);
            return entity;
        }

        query.getConditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, collection))
                .map(DocumentCondition::getDocument)
                .forEach(entity::add);
        return entity;
    }
}
