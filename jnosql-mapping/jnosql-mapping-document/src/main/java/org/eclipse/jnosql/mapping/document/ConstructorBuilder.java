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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.mapping.MappingException;
import org.eclipse.jnosql.mapping.reflection.ConstructorEvent;
import org.eclipse.jnosql.mapping.reflection.ConstructorMetadata;
import org.eclipse.jnosql.mapping.reflection.ParameterMetaData;

import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

        try {
            Instance<Event<ConstructorEvent>> instance = CDI.current().select(new TypeLiteral<>() {
            });
            Event<ConstructorEvent> event = instance.get();
            event.fire(ConstructorEvent.of(constructor, values.toArray()));
            return (T) constructor.newInstance(values.toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new MappingException("There is an issue to create a new instance of this class" +
                    " using this constructor: " + constructor, e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConstructorBuilder that = (ConstructorBuilder) o;
        return Objects.equals(values, that.values) && Objects.equals(metadata, that.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values, metadata);
    }

    @Override
    public String toString() {
        return "ConstructorBuilder{" +
                "values=" + values +
                ", metadata=" + metadata +
                '}';
    }
}
