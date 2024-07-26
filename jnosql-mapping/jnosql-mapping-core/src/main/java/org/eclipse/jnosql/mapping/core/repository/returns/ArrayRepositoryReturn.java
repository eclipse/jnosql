/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.core.repository.returns;

import org.eclipse.jnosql.mapping.core.repository.DynamicReturn;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayRepositoryReturn extends AbstractRepositoryReturn {

    public ArrayRepositoryReturn() {
        super(null);
    }

    @Override
    public boolean isCompatible(Class<?> entity, Class<?> returnType) {
        return returnType.isArray();
    }

    @Override
    public <T> Object convert(DynamicReturn<T> dynamicReturn) {
        return dynamicReturn.result().collect(Collectors.toList());
    }

    @Override
    public <T> Object convertPageRequest(DynamicReturn<T> dynamicReturn) {
        return dynamicReturn.streamPagination().collect(Collectors.toList());
    }
}
