/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.metadata;

import jakarta.nosql.Entity;
import org.eclipse.jnosql.mapping.Embeddable;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;

/**
 * enum that contains kinds of annotations to either fields constructor parameters on java.
 */
public enum MappingType {
    EMBEDDED, MAP, COLLECTION, DEFAULT, ENTITY;


    /**
     * select you the kind of annotation on field and then define a enum type, follow the sequences:
     * <ul>
     * <li>Collection</li>
     * <li>Map</li>
     * <li>embedded</li>
     * </ul>.
     *
     * @param type - the type
     * @return the type
     */
    public static MappingType of(Class<?> type) {
        if (Collection.class.isAssignableFrom(type)) {
            return MappingType.COLLECTION;
        }
        if (Map.class.isAssignableFrom(type)) {
            return MappingType.MAP;
        }
        if (type.isAnnotationPresent(Embeddable.class)) {
            return MappingType.EMBEDDED;
        }
        if (type.isAnnotationPresent(Entity.class)) {
            return MappingType.ENTITY;
        }

        return MappingType.DEFAULT;
    }
}