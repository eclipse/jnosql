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
package org.jnosql.artemis.keyvalue;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplateProducer;
import org.jnosql.artemis.CDIExtension;
import jakarta.nosql.keyvalue.BucketManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(CDIExtension.class)
public class DefaultKeyValueTemplateProducerTest {


    @Inject
    private KeyValueTemplateProducer producer;


    @Test
    public void shouldReturnErrorWhenManagerNull() {
        assertThrows(NullPointerException.class, () -> producer.get(null));
    }

    @Test
    public void shouldReturn() {
        BucketManager manager = Mockito.mock(BucketManager.class);
        KeyValueTemplate repository = producer.get(manager);
        assertNotNull(repository);
    }
}