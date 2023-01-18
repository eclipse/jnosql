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


import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.JSONQueryValue;

import java.util.List;


/**
 * A template class to Update and Insert query parser to extract the condition
 */
abstract class ConditionQueryParser {

    protected DocumentEntity getEntity(ConditionQuerySupplier query, String collection, Params params,
                                       DocumentObserverParser observer) {
        DocumentEntity entity = DocumentEntity.of(collection);

        if (query.useJSONCondition()) {
            JSONQueryValue jsonValue = query.value().orElseThrow(() -> new QueryException("It is an invalid state of" +
                    " either Update or Insert."));
            List<Document> documents = JsonObjects.getDocuments(jsonValue.get());
            entity.addAll(documents);
            return entity;
        }

        query.conditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, collection))
                .map(DocumentCondition::document)
                .forEach(entity::add);
        return entity;
    }
}
