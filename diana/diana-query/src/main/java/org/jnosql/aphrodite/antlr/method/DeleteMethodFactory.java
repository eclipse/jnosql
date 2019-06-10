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

import org.jnosql.query.DeleteQuery;

import java.lang.reflect.Method;
import java.util.function.BiFunction;

/**
 * A {@link DeleteQuery} factory from {@link Method}, this class create an instance of  DeleteQuery from the {@link Method#getName()}
 * nomenclature convention. It extends a {@link BiFunction} where:
 * - The Method
 * - The entity name
 * - The DeleteQuery from both Method and entity name
 */
public interface DeleteMethodFactory extends BiFunction<Method, String, DeleteQuery> {

    /**
     * Returns a default implementation of {@link DeleteMethodFactory}
     *
     * @return {@link DeleteMethodFactory}
     */
    static DeleteMethodFactory get() {
        return DeleteMethodFactorySupplier.INSTANCE;
    }

}
