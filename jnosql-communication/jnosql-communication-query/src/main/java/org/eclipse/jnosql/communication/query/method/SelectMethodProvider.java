/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query.method;

import jakarta.nosql.query.SelectQuery;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * A {@link SelectQuery} factory from {@link Method}, this class create an instance of  SelectQuery from the {@link Method#getName()}
 * nomenclature convention. It extends a {@link BiFunction} where:
 * - The Method
 * - The entity name
 * - The SelectQuery from both Method and entity name
 */
public interface SelectMethodProvider extends BiFunction<Method, String, SelectQuery> {

    /**
     * Returns a default implementation of
     *
     * @return a  instance from SPI
     */
    static SelectMethodProvider get() {
        return DefaultSelectMethodProvider.INSTANCE;
    }
}
