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

import java.time.Duration;
import java.util.Optional;

/**
 * To either insert or overrides values from a key-value database use the <b>PUT</b> statement.
 */
public interface PutQuery extends Query {

    /**
     * The key
     * @return the key
     */
    Value<?> getKey();

    /**
     * The value
     * @return the value
     */
    Value<?> getValue();

    /**
     * This duration set a time for data in an entity to expire. It defines the time to live of an object in a database.
     * @return the TTL otherwise {@link Optional#empty()}
     */
    Optional<Duration> getTtl();
}
