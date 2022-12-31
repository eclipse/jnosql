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
package org.eclipse.jnosql.mapping.validation;

import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnManager;
import org.mockito.Mockito;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import javax.interceptor.Interceptor;
import java.math.BigDecimal;
import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static org.mockito.Mockito.when;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
class ColumnMockProducer implements Supplier<ColumnManager> {

    @Override
    @Produces
    public ColumnManager get() {
        ColumnManager manager = Mockito.mock(ColumnManager.class);

        ColumnEntity entity = ColumnEntity.of("person");
        entity.add(Column.of("name", "Ada"));
        entity.add(Column.of("age", 30));
        entity.add(Column.of("salary", BigDecimal.TEN));
        entity.add(Column.of("phones", singletonList("22342342")));
        when(manager.insert(Mockito.any(ColumnEntity.class))).thenReturn(entity);
        when(manager.update(Mockito.any(ColumnEntity.class))).thenReturn(entity);

        return manager;
    }
}
