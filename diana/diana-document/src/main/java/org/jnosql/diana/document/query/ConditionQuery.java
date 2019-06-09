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
package org.jnosql.diana.document.query;

import org.jnosql.query.Condition;
import org.jnosql.query.JSONValue;

import java.util.List;
import java.util.Optional;

/**
 * A base supplier to {@link org.jnosql.query.InsertQuery} and
 * {@link org.jnosql.query.UpdateQuery}
 */
interface ConditionQuerySupplier {

    List<Condition> getConditions();

    Optional<JSONValue> getValue();

    default boolean useJSONCondition() {
        return getConditions().isEmpty() && getValue().isPresent();
    }

}
