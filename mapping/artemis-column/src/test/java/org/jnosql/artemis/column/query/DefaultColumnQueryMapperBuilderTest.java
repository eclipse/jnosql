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
package org.jnosql.artemis.column.query;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.model.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(CDIExtension.class)
public class DefaultColumnQueryMapperBuilderTest {


    @Inject
    private ColumnQueryMapperBuilder mapperBuilder;


    @Test
    public void shouldReturnErrorWhenEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> mapperBuilder.selectFrom(null));
    }

    @Test
    public void shouldReturnSelectFrom() {
        ColumnMapperFrom columnFrom = mapperBuilder.selectFrom(Person.class);
        assertNotNull(columnFrom);
    }

    @Test
    public void shouldReturnErrorWhenDeleteEntityClassIsNull() {
        assertThrows(NullPointerException.class, () -> mapperBuilder.deleteFrom(null));
    }

    @Test
    public void shouldReturnDeleteFrom() {
        ColumnMapperDeleteFrom columnDeleteFrom = mapperBuilder.deleteFrom(Person.class);
        assertNotNull(columnDeleteFrom);
    }
}