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

import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.tck.entities.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class DefaultColumnWorkflowTest {


    @InjectMocks
    private DefaultColumnWorkflow subject;

    @Mock
    private ColumnEventPersistManager columnEventPersistManager;

    @Mock
    private ColumnEntityConverter converter;

    @Mock
    private ColumnEntity columnEntity;

    @Test
    public void shouldReturnErrorWhenEntityIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            UnaryOperator<ColumnEntity> action = t -> t;
            subject.flow(null, action);
        });
    }

    @Test
    public void shouldReturnErrorWhenActionIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.flow("", null));
    }

    @Test
    public void shouldFollowWorkflow() {
        UnaryOperator<ColumnEntity> action = t -> t;
        subject.flow(Person.builder().withId(1L).withAge().withName("Ada").build(), action);

        verify(columnEventPersistManager).firePreColumn(any());
        verify(columnEventPersistManager).firePostColumn(any());
        verify(columnEventPersistManager).firePreEntity(any());
        verify(columnEventPersistManager).firePostEntity(any());

        verify(columnEventPersistManager).firePreColumnEntity(any());
        verify(columnEventPersistManager).firePostColumnEntity(any());
        verify(converter).toColumn(any());
    }

}