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

/**
 * This interface represents a collection of
 * params in a dynamic {@link Query}. When a query has one or more {@link ParamValue}, the {@link Params} will collect it.
 */
public interface Params {

    /**
     * It returns either if the Params is empty or there isn't left params to fill out.
     *
     * @return true if Params is empty
     */
    boolean isEmpty();

    /**
     * The list of params names the left to fill out in the Params.
     *
     * @return the names params
     */
    List<String> getNames();

    /**
     * Binds the param name to respective value.
     *
     * @param name  the param name
     * @param value the value to bind
     * @throws NullPointerException when there is null parameters
     */
    void bind(String name, Object value);
}
