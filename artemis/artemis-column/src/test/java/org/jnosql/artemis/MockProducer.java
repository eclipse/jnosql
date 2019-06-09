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
package org.jnosql.artemis;


import org.jnosql.diana.column.Column;
import org.jnosql.diana.column.ColumnEntity;
import org.jnosql.diana.column.ColumnFamilyManager;
import org.jnosql.diana.column.ColumnFamilyManagerAsync;
import org.mockito.Mockito;

import javax.enterprise.inject.Produces;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockProducer {


    @Produces
    public ColumnFamilyManager getColumnFamilyManager() {
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add(Column.of("name", "Default"));
        entity.add(Column.of("age", 10));
        ColumnFamilyManager manager = mock(ColumnFamilyManager.class);
        when(manager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        return manager;

    }

    @Produces
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    public ColumnFamilyManager getColumnFamilyManagerMock() {
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add(Column.of("name", "columnRepositoryMock"));
        entity.add(Column.of("age", 10));
        ColumnFamilyManager manager = mock(ColumnFamilyManager.class);
        when(manager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        return manager;

    }
    @Produces
    public ColumnFamilyManagerAsync getColumnFamilyManagerAsync() {
        return Mockito.mock(ColumnFamilyManagerAsync.class);
    }


    @Produces
    @Database(value = DatabaseType.COLUMN, provider = "columnRepositoryMock")
    public ColumnFamilyManagerAsync getColumnFamilyManagerAsyncMock() {
        return Mockito.mock(ColumnFamilyManagerAsync.class);
    }


}
