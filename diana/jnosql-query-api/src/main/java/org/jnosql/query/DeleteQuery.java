/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.query;

import java.util.List;
import java.util.Optional;

/**
 * Deleting either an entity or fields uses the <b>DELETE</b> statement
 */
public interface DeleteQuery extends Query {

    /**
     * The fields that will delete in this query, if this fields is empty, this query will remove the whole entity.
     * @return the fields list
     */
    List<String> getFields();

    /**
     * The entity name
     * @return the entity name
     */
    String getEntity();

    /**
     * The condition at this {@link DeleteQuery}, if the Where is empty that means will delete the whole entities.
     * @return the {@link Where} entity otherwise {@link Optional#empty()}
     */
    Optional<Where> getWhere();

}
