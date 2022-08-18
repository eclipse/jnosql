/*
 *  Copyright (c) 2022 Otávio Santana and others
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
package org.eclipse.jnosql.mapping.reflection;

import java.lang.reflect.Constructor;
import java.util.Comparator;

/**
 * This Comparator defines the priority of the entity's constructor that JNoSQL will use as a priority.
 * The emphasis will be on a default constructor, a non-arg-param constructor, either default or public,
 * and then the constructor with more visibility.
 * @param <T> the entity type
 */
class ConstructorComparable<T> implements Comparator<Constructor<T>> {

    @Override
    public int compare(Constructor<T> constructorA, Constructor<T> constructorB) {
        int parameterCount = constructorA.getParameterCount();
        int parameterCountB = constructorB.getParameterCount();
        return 0;
    }
}
