/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.repository.returns;

import org.eclipse.jnosql.mapping.repository.RepositoryReturn;

abstract class AbstractRepositoryReturn implements RepositoryReturn {

    private final Class<?> typeClass;

    protected AbstractRepositoryReturn(Class<?> typeClass) {
        this.typeClass = typeClass;
    }

    @Override
    public boolean isCompatible(Class<?> entityClass, Class<?> returnType) {
        return typeClass.equals(returnType);
    }

}
