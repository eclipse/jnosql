/*
 *  Copyright (c) 2019 Otávio Santana and others
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
import java.util.Collections;

final class ConstructorMetadataBuilder {

    private final Reflections reflections;

    ConstructorMetadataBuilder(Reflections reflections) {
        this.reflections = reflections;
    }

    <T> ConstructorMetadata build(Class<T> entity) {
        Constructor<T> constructor = reflections.getConstructor(entity);
        if(constructor.getParameterCount() == 0) {
            return new ConstructorMetadata(constructor, Collections.emptyList());
        }
        return null;
    }
}
