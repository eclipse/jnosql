/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.reflection.metadatas;

import jakarta.nosql.mapping.Entity;
import org.eclipse.jnosql.mapping.reflection.EntityAnnotationReader;
import org.eclipse.jnosql.mapping.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * an implementation that reads an entity classs from {@link Entity}
 */
public final class JakartaNoSQLEntityReader implements EntityAnnotationReader {

    @Override
    public String name(Class<?> entity) {
        return Optional.ofNullable((Entity) entity.getAnnotation(Entity.class))
                .map(Entity::value)
                .filter(StringUtils::isNotBlank)
                .orElse(entity.getSimpleName());
    }

    @Override
    public boolean test(Class<?> type) {
        return type.getAnnotation(Entity.class) != null;
    }

    @Override
    public Class<? extends Annotation> get() {
        return Entity.class;
    }
}
