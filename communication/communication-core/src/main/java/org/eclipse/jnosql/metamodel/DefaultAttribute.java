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
 *   Alessandro Moscatelli
 */
package org.eclipse.jnosql.metamodel;

import jakarta.nosql.metamodel.Attribute;
import org.eclipse.jnosql.AbstractGenericType;

/**
 * Default metamodel attribute implementation
 * @param <X> The Entity type the attribute belongs to
 * @param <T> The attribute type
 */
public abstract class DefaultAttribute<X, T> extends AbstractGenericType<X> implements Attribute<X, T> {

    private final Class<T> attributeType;
    private final String name;
    
    public DefaultAttribute(Class<X> type, Class<T> attributeType, String name) {
        super(type);
        this.attributeType = attributeType;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Class<T> getAttributeType() {
        return attributeType;
    }
    
}
