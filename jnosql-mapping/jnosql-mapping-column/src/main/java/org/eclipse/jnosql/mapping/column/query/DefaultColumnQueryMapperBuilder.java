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
package org.eclipse.jnosql.mapping.column.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnQueryMapper;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

import static java.util.Objects.requireNonNull;

@ApplicationScoped
class DefaultColumnQueryMapperBuilder implements ColumnQueryMapper {

    @Inject
    private Instance<EntitiesMetadata> mappings;

    @Inject
    private Instance<Converters> converters;

    @Override
    public <T> ColumnMapperFrom selectFrom(Class<T> type) {
        requireNonNull(type, "entity is required");
        EntityMetadata mapping = mappings.get().get(type);
        return new ColumnMapperSelect(mapping, converters.get());
    }

    @Override
    public <T> ColumnMapperDeleteFrom deleteFrom(Class<T> type) {
        requireNonNull(type, "entity is required");
        EntityMetadata mapping = mappings.get().get(type);
        return new DefaultColumnMapperDeleteBuilder(mapping, converters.get());
    }
}
