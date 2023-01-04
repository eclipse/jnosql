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
package org.eclipse.jnosql.mapping.column;


import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnManager;
import jakarta.nosql.mapping.Database;
import jakarta.nosql.mapping.DatabaseType;
import org.mockito.Mockito;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.interceptor.Interceptor;

import java.util.function.Supplier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class MockProducer implements Supplier<ColumnManager> {


    @Produces
    @Override
    public ColumnManager get() {
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add(Column.of("name", "Default"));
        entity.add(Column.of("age", 10));
        ColumnManager manager = mock(ColumnManager.class);
        when(manager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        return manager;

    }

    @Produces
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    public ColumnManager getColumnManagerMock() {
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add(Column.of("name", "columnRepositoryMock"));
        entity.add(Column.of("age", 10));
        ColumnManager manager = mock(ColumnManager.class);
        when(manager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        return manager;

    }

}
