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
package org.jnosql.artemis.document;

import org.jnosql.artemis.model.Person;
import jakarta.nosql.document.DocumentEntity;
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
public class DefaultDocumentWorkflowTest {


    @InjectMocks
    private DefaultDocumentWorkflow subject;

    @Mock
    private DocumentEventPersistManager columnEventPersistManager;

    @Mock
    private DocumentEntityConverter converter;

    @Mock
    private DocumentEntity documentEntity;


    @Test
    public void shouldReturnErrorWhenEntityIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            UnaryOperator<DocumentEntity> action = t -> t;
            subject.flow(null, action);
        });
    }

    @Test
    public void shouldReturnErrorWhenActionIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.flow("", null));
    }

    @Test
    public void shouldFollowWorkflow() {
        UnaryOperator<DocumentEntity> action = t -> t;
        subject.flow(Person.builder().withId(1L).withAge().withName("Ada").build(), action);

        verify(columnEventPersistManager).firePreDocument(any());
        verify(columnEventPersistManager).firePostDocument(any());
        verify(columnEventPersistManager).firePreEntity(any());
        verify(columnEventPersistManager).firePostEntity(any());
        verify(columnEventPersistManager).firePreDocumentEntity(any());
        verify(columnEventPersistManager).firePostDocumentEntity(any());
        verify(converter).toDocument(any(Object.class));
    }

}