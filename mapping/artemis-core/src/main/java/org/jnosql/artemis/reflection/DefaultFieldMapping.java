/*
 *  Copyright (c) 2017 Otávio Santana and others
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


import org.jnosql.artemis.AttributeConverter;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * Class that represents {@link FieldMapping} a default field
 */
public class DefaultFieldMapping extends AbstractFieldMapping {


    private final boolean id;

    DefaultFieldMapping(FieldType type, Field field, String name,
                        Class<? extends AttributeConverter> converter, boolean id,
                        FieldReader reader, FieldWriter writer) {
        super(type, field, name, converter, reader, writer);
        this.id = id;
    }

    @Override
    public boolean isId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractFieldMapping that = (AbstractFieldMapping) o;
        return type == that.type &&
                Objects.equals(field, that.field) &&
                Objects.equals(name, that.name);
    }


    @Override
    public int hashCode() {
        return Objects.hash(type, field, name);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultFieldMapping{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", field=").append(field);
        sb.append(", name='").append(name).append('\'');
        sb.append(", fieldName='").append(fieldName).append('\'');
        sb.append(", converter=").append(converter);
        sb.append('}');
        return sb.toString();
    }
}
