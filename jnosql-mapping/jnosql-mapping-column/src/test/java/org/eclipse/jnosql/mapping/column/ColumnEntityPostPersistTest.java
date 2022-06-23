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
package org.eclipse.jnosql.mapping.column;

import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.column.ColumnEntityPostPersist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ColumnEntityPostPersistTest {


    @Test
    public void shouldReturnNPEWhenEntityIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> new DefaultColumnEntityPostPersist(null));
    }

    @Test
    public void shouldReturnInstance() {
        ColumnEntity entity = ColumnEntity.of("columnFamily");
        ColumnEntityPostPersist postPersist = new DefaultColumnEntityPostPersist(entity);
        assertEquals(entity, postPersist.getEntity());
    }
}