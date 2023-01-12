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
package org.eclipse.jnosql.communication.column;


import org.eclipse.jnosql.communication.query.JSONQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;

import java.util.List;
import java.util.Optional;

/**
 * A base supplier to {@link org.eclipse.jnosql.communication.query.InsertQuery} and
 * {@link org.eclipse.jnosql.communication.query.UpdateQuery}
 */
interface ConditionQuerySupplier {

    List<QueryCondition> conditions();

    Optional<JSONQueryValue> value();

    default boolean useJSONCondition() {
        return conditions().isEmpty() && value().isPresent();
    }

}