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
import java.util.Objects;

enum SelectMethodFactorySupplier implements SelectMethodFactory {

    INSTANCE;

    @Override
    public SelectQuery apply(Method method, String entity) {
        Objects.requireNonNull(method, "method is required");
        Objects.requireNonNull(entity, "entity is required");
        FindByMethodQuerySupplier supplier = new FindByMethodQuerySupplier();
        return supplier.apply(method.getName(), entity);
    }
}
