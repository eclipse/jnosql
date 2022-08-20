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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.ParameterMetaData;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

class ConstructorBuilder {

    private final List<Object> values = new ArrayList<>();

    private final ConstructorMetadata metadata;

    ConstructorBuilder(ConstructorMetadata metadata) {
        this.metadata = metadata;
    }


    public List<ParameterMetaData> getParameters() {
        return this.metadata.getParameters();
    }

    public void add(Object value) {
        this.values.add(value);
    }

    public void addEmptyParameter() {
        this.values.add(null);
    }

    public <T> T build() {
        Constructor<?> constructor = metadata.getConstructor();

        //fire an event here later
        try {
            return (T) constructor.newInstance(values.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MappingException("There is an issue to create a new instance of this class" +
                    " using this constructor: " + constructor, e);
        }
    }
}
