/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.keyvalue.reactive;

import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.inject.Inject;

@CDIExtension
class ReactiveKeyValueTemplateProducerTest {

    @Inject
    private ReactiveKeyValueTemplateProducer producer;

    @Test
    public void shouldReturnTemplate() {
        final KeyValueTemplate template = Mockito.mock(KeyValueTemplate.class);
        final ReactiveKeyValueTemplate reactiveTemplate = producer.get(template);
        Assertions.assertNotNull(reactiveTemplate);
    }
    
    @Test
    public void shouldReturnNPEWhenTemplateIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> producer.get(null));
    }
}