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


import org.eclipse.jnosql.communication.query.SelectQuery;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiFunction;

public enum SelectMethodProvider implements BiFunction<Method, String, SelectQuery> {

    INSTANCE;


    @Override
    public SelectQuery apply(Method method, String entity) {
        Objects.requireNonNull(method, "method is required");
        Objects.requireNonNull(entity, "entity is required");
        SelectMethodQueryProvider supplier = new SelectMethodQueryProvider();
        return supplier.apply(method.getName(), entity);
    }
}
