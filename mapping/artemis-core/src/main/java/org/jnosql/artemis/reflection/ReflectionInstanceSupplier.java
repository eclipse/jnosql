/*
 *  Copyright (c) 2018 Otávio Santana and others
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
package org.jnosql.artemis.reflection;

import java.lang.reflect.Constructor;

class ReflectionInstanceSupplier implements InstanceSupplier {

    private final Reflections reflections;

    private final Constructor<?> constructor;

    public ReflectionInstanceSupplier(Reflections reflections, Constructor<?> constructor) {
        this.reflections = reflections;
        this.constructor = constructor;
    }

    @Override
    public Object get() {
        return reflections.newInstance(constructor);
    }
}
