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
package org.jnosql.aphrodite.antlr.method;

import org.jnosql.query.SelectQuery;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * A {@link SelectQuery} factory from {@link Method}, this class create an instance of  SelectQuery from the {@link Method#getName()}
 * nomenclature convention. It extends a {@link BiFunction} where:
 * - The Method
 * - The entity name
 * - The SelectQuery from both Method and entity name
 */
public interface SelectMethodFactory extends BiFunction<Method, String, SelectQuery> {

    /**
     * Returns a default implementation of {@link SelectMethodFactory}
     *
     * @return {@link SelectMethodFactory}
     */
    static SelectMethodFactory get() {
        return SelectMethodFactorySupplier.INSTANCE;
    }
}
