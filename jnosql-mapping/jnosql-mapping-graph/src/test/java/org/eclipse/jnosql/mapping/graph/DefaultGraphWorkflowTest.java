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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.graph.entities.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultGraphWorkflowTest {


    @InjectMocks
    private DefaultGraphWorkflow subject;

    @Mock
    private GraphEventPersistManager graphEventPersistManager;

    @Mock
    private GraphConverter converter;

    @Mock
    private Vertex vertex;


    @Test
    public void shouldReturnErrorWhenEntityIsNull() {
        assertThrows(NullPointerException.class, () -> {
            UnaryOperator<Vertex> action = t -> t;
            subject.flow(null, action);
        });
    }

    @Test
    public void shouldReturnErrorWhenActionIsNull() {
        assertThrows(NullPointerException.class, () -> subject.flow("", null));
    }

    @Test
    public void shouldFollowWorkflow() {
        UnaryOperator<Vertex> action = t -> t;
        subject.flow(Person.builder().withId(1L).withAge().withName("Ada").build(), action);

        verify(graphEventPersistManager).firePreGraph(any());
        verify(graphEventPersistManager).firePostGraph(any());
        verify(graphEventPersistManager).firePreEntity(any());
        verify(graphEventPersistManager).firePostEntity(any());

        verify(graphEventPersistManager).firePreGraphEntity(any());
        verify(graphEventPersistManager).firePostGraphEntity(any());
        verify(converter).toVertex(any());
    }

}