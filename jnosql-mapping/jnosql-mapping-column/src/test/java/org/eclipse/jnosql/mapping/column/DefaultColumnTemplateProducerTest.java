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

import org.eclipse.jnosql.communication.column.ColumnManager;
import jakarta.nosql.column.ColumnTemplate;
import org.eclipse.jnosql.mapping.test.jupiter.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@CDIExtension
public class DefaultColumnTemplateProducerTest {

    @Inject
    private ColumnTemplateProducer producer;


    @Test
    public void shouldReturnErrorWhenColumnManagerNull() {
        Assertions.assertThrows(NullPointerException.class, () -> producer.apply(null));
    }

    @Test
    public void shouldReturn() {
        ColumnManager manager = Mockito.mock(ColumnManager.class);
        ColumnTemplate columnTemplate = producer.apply(manager);
        assertNotNull(columnTemplate);
    }
}