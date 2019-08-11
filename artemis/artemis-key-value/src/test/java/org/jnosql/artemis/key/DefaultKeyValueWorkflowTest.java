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
package org.jnosql.artemis.key;

import jakarta.nosql.kv.KeyValueEntity;
import jakarta.nosql.mapping.kv.KeyValueEntityConverter;
import jakarta.nosql.mapping.kv.KeyValueEventPersistManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.UnaryOperator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class DefaultKeyValueWorkflowTest {

    @Mock
    private KeyValueEventPersistManager eventPersistManager;

    @Mock
    private KeyValueEntityConverter converter;

    @InjectMocks
    private DefaultKeyValueWorkflow subject;

    @Mock
    private KeyValueEntity keyValueEntity;

    @BeforeEach
    public void setUp() {
        when(converter.toKeyValue(any(Object.class)))
                .thenReturn(keyValueEntity);

    }

    @Test
    public void shouldFollowWorkflow() {
        UnaryOperator<KeyValueEntity> action = t -> t;
        subject.flow("entity", action);

        verify(eventPersistManager).firePreKeyValue(any(KeyValueEntity.class));
        verify(eventPersistManager).firePostKeyValue(any(KeyValueEntity.class));
    }

}