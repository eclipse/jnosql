/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import org.eclipse.jnosql.communication.Params;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.query.JSONQueryValue;

import java.util.List;

/**
 * A template class to Update and Insert query parser to extract the condition
 */
abstract class ConditionQueryParser {


    protected CommunicationEntity getEntity(ConditionQuerySupplier query, String columnFamily, Params params,
                                            CommunicationObserverParser observer) {
        CommunicationEntity entity = CommunicationEntity.of(columnFamily);

        if (query.useJSONCondition()) {
            JSONQueryValue jsonValue = query.value()
                    .orElseThrow(() -> new QueryException("It is an invalid state of" +
                    " either Update or Insert."));
            List<Element> elements = JsonObjects.getColumns(jsonValue.get());
            entity.addAll(elements);
            return entity;
        }

        query.conditions()
                .stream()
                .map(c -> Conditions.getCondition(c, params, observer, columnFamily))
                .map(CriteriaCondition::element)
                .forEach(entity::add);
        return entity;
    }
}
